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

class JdbcSessionsRepo(private val dataSource: DataSource) : SessionRepo {
    override fun createSession(dto: SessionDTO): Int {
        dataSource.connection.use {
            val query = "INSERT INTO Session(capacity, session_date, closed, game_id) VALUES (?, ?, ?, ?)"
            val statement = it.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)

            statement.bindParameters(dto.capacity, dto.date, dto.closed, dto.game)
                .executeUpdate()

            val result = statement.generatedKeys
            if (result.next()) {
                val sid = result.getInt(1)

                val query2 = "INSERT INTO SessionPlayer(session_id, player_id) VALUES (?, ?)"
                it.prepareStatement(query2)
                    .bindParameters(sid, dto.players[0])
                    .executeUpdate()

                return sid
            } else throw AppException.SQLException("Session not inserted")
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

    override fun updateSession(sid: Int, date: String, capacity: Int) {
        dataSource.connection.use {
            val query = "UPDATE Session SET session_date = ?, capacity = ? WHERE sid = ?"
            it.prepareStatement(query)
                .bindParameters(date, capacity, sid)
                .executeUpdate()
        }
    }

    override fun deleteSession(sid: Int) {
        dataSource.connection.use {
            val query = "DELETE FROM SessionPlayer WHERE session_id = ?"
            it.prepareStatement(query)
                .bindParameters(sid)
                .executeUpdate()

            val query2 = "DELETE FROM Session WHERE sid = ?"
            it.prepareStatement(query2)
                .bindParameters(sid)
                .executeUpdate()
        }
    }

    override fun deletePlayerFromSession(sid: Int, pid: Int) {
        dataSource.connection.use {
            val query = "DELETE FROM SessionPlayer WHERE session_id = ? AND player_id = ?"
            it.prepareStatement(query)
                .bindParameters(sid, pid)
                .executeUpdate()
        }
    }

    override fun getListOfSessions(
        gid: Int?,
        date: String?,
        state: Boolean?,
        pid: Int?,
        skip: Int,
        limit: Int
    ): Pair<List<Session>, Int> {
        dataSource.connection.use {

            val result1 = it.setupListSessionsStatement(gid, date, state, pid)
                .bindParameters(gid, date, state, pid)
                .executeQuery()

            val sessions = mutableListOf<Session>()

            while (result1.next()) {
                val sid = result1.getInt("sid")

                val query1 = "SELECT player_id FROM SessionPlayer WHERE session_id = ?"
                val result2 = it.prepareStatement(query1)
                    .bindParameters(sid)
                    .executeQuery()

                val players = result2.toPlayersList()
                sessions.add(
                    Session(
                        id = sid,
                        capacity = result1.getInt("capacity"),
                        date = result1.getString("session_date"),
                        game = result1.getInt("game_id"),
                        closed = result1.getBoolean("closed"),
                        players = players
                    )
                )
            }

            return sessions.drop(skip).take(limit) to sessions.size
        }
    }

    private fun ResultSet.toPlayersList(): List<Int> = buildList {
        while (next()) {
            add(getInt("player_id"))
        }
    }

    private fun Connection.setupListSessionsStatement(
        gid: Int?,
        date: String?,
        state: Boolean?,
        pid: Int?,
    ): PreparedStatement = prepareStatement(
        buildString {
            var firstNonNull = true
            append("SELECT * FROM Session")
            if (gid != null) {
                append(" WHERE")
                append(" game_id = ?")
                firstNonNull = false
            }
            if (date != null) {
                append(if (firstNonNull) " WHERE" else " AND")
                append(" session_date = ?")
                firstNonNull = false
            }
            if (state != null) {
                append(if (firstNonNull) " WHERE" else " AND")
                append(" closed = ?")
                firstNonNull = false
            }
            if (pid != null) {
                append(if (firstNonNull) " WHERE" else " AND")
                append(" sid IN (SELECT session_id FROM SessionPlayer WHERE player_id = ?)")
            }
            //append(" OFFSET ? LIMIT ?")
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