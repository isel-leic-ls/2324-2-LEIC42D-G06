package pt.isel.ls.repo.jdbc

import SessionRepo
import pt.isel.ls.AppException
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionDTO
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource

class JdbcSessionsRepo(private val dataSource : DataSource) : SessionRepo {
    override fun createSession(dto: SessionDTO): Int {
        dataSource.connection.use {
            val query = "INSERT INTO Session(capacity, session_date, closed, game_id) VALUES (?, ?, ?, ?)"
            val statement = it.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

            statement.bindParameters(dto.capacity, dto.date, dto.closed, dto.game)
                .executeUpdate()

            val result = statement.generatedKeys
            if(result.next()) {
                val sid = result.getInt(1)

                val query2 = "INSERT INTO SessionPlayer(session_id, player_id) VALUES (?, ?)"
                it.prepareStatement(query2)
                    .bindParameters(sid, dto.players[0])
                    .executeUpdate()

                return sid
            }
            else throw AppException.SQLException("Session not inserted")
        }
    }

    override fun addPlayerToSession(sid: Int, pid: Int) {
        // handled with triggers in the database
        // this throws exception if 2 players try to join the same session at the same time
        dataSource.connection.use {
            val query = "INSERT INTO SessionPlayer(session_id, player_id) VALUES (?, ?)"
            it.prepareStatement(query)
                .bindParameters(sid, pid)
                .executeUpdate()
        }
    }

    override fun getSession(sid: Int): Session {
        dataSource.connection.use {
            val query = "SELECT * FROM Session WHERE sid = ?"
            val result = it.prepareStatement(query)
                .bindParameters(sid)
                .executeQuery()

            if (!result.next()) throw AppException.SessionNotFound("Session $sid does not exist")

            val query2 = "SELECT player_id FROM SessionPlayer WHERE session_id = ?"

            val result2 = it.prepareStatement(query2)
                .bindParameters(sid)
                .executeQuery()

            val players = result2.toPlayersList()
            return Session(
                id = sid,
                capacity = result.getInt("capacity"),
                date = result.getString("session_date"),
                game = result.getInt("game_id"),
                closed = result.getBoolean("closed"),
                players = players
            )
        }
    }

    override fun getListOfSessions(
        gid: Int,
        date: String?,
        state: Boolean?,
        pid: Int?,
        skip: Int,
        limit: Int
    ): List<Session> {
        dataSource.connection.use {

            val result = it.setupListSessionsStatement(date, state, pid)
                .bindParameters(gid, date, state, pid, limit, skip)
                .executeQuery()

            val sessions = mutableListOf<Session>()

            while(result.next()) {
                val sid = result.getInt("sid")

                val query = "SELECT player_id FROM SessionPlayer WHERE session_id = ?"
                val result2 = it.prepareStatement(query)
                    .bindParameters(sid)
                    .executeQuery()

                val players = result2.toPlayersList()
                sessions.add(
                    Session(
                        id = sid,
                        capacity = result.getInt("capacity"),
                        date = result.getString("session_date"),
                        game = result.getInt("game_id"),
                        closed = result.getBoolean("closed"),
                        players = players
                    )
                )
            }
            return sessions
        }
    }

    private fun ResultSet.toPlayersList(): List<Int> = buildList {
        while (next()) { add(getInt("player_id")) }
    }

    private fun Connection.setupListSessionsStatement(
        date: String?,
        state: Boolean?,
        pid: Int?,
    ): PreparedStatement = prepareStatement(
        buildString {
            append("SELECT * FROM Session WHERE game_id = ?")
            if(date != null) append(" AND session_date = ?")
            if(state != null) append(" AND closed = ?")
            if(pid != null) append(" AND sid IN (SELECT session_id FROM SessionPlayer WHERE player_id = ?)")
            append(" LIMIT ? OFFSET ?")
        })

    override fun checkSessionExists(sid: Int): Boolean {
        dataSource.connection.use {
            val query = "SELECT * FROM Session WHERE sid = ?"
            val result = it.prepareStatement(query)
                .bindParameters(sid)
                .executeQuery()
            return result.next()
        }
    }
}