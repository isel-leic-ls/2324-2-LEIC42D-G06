package pt.isel.ls.repo.jdbc

import SessionRepo
import pt.isel.ls.AppException
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionDTO
import java.sql.Statement
import javax.sql.DataSource

class JdbcSessionsRepo(private val dataSource : DataSource) : SessionRepo {
    override fun createSession(sessionDTO: SessionDTO): Int {
        dataSource.connection.use {
            val statement = it.prepareStatement(
                "INSERT INTO Session(capacity, session_date, closed, game_id) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            )

            statement.setInt(1, sessionDTO.capacity)
            statement.setString(2, sessionDTO.date)
            statement.setBoolean(3, sessionDTO.closed)
            statement.setInt(4, sessionDTO.game)
            statement.executeUpdate()

            val rs = statement.generatedKeys
            if(rs.next()) {
                val sid = rs.getInt(1)
                val statement2 = it.prepareStatement(
                    "INSERT INTO SessionPlayer(session_id, player_id) VALUES (?, ?)"
                )
                statement2.setInt(1, sid)
                statement2.setInt(2, sessionDTO.players[0])
                statement2.executeUpdate()
                return sid
            }
            else throw AppException.SQLException("Session not inserted")
        }
    }

    override fun addPlayerToSession(sid: Int, pid: Int) {
        // handled with triggers in the database
        // this throws exception if 2 players try to join the same session at the same time
        dataSource.connection.use {
            val statement = it.prepareStatement(
                "INSERT INTO SessionPlayer(session_id, player_id) VALUES (?, ?)"
            )
            statement.setInt(1, sid)
            statement.setInt(2, pid)
            statement.executeUpdate()
            return
        }
    }

    override fun getSession(sid: Int): Session {
        dataSource.connection.use {
            val statement = it.prepareStatement(
                "SELECT * FROM Session WHERE sid = ?"
            )
            statement.setInt(1, sid)
            val result = statement.executeQuery()
            if (!result.next()) throw AppException.SessionNotFound("Session $sid does not exist")

            val statement2 = it.prepareStatement(
                "SELECT player_id FROM SessionPlayer WHERE session_id = ?"
            )
            statement2.setInt(1, sid)
            val result2 = statement2.executeQuery()

            val players = mutableListOf<Int>()
            while(result2.next()) {
                players.add(result2.getInt("player_id"))
            }

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
            var query = "SELECT * FROM Session WHERE game_id = ?"
            if(date != null) query += " AND session_date = ?"
            if(state != null) query += " AND closed = ?"
            if(pid != null) query += " AND sid IN (SELECT session_id FROM SessionPlayer WHERE player_id = ?)"
            query += " LIMIT ? OFFSET ?"

            val statement = it.prepareStatement(query)
            statement.setInt(1, gid)
            var index = 2
            if(date != null) statement.setString(index++, date)
            if(state != null) statement.setBoolean(index++, state)
            if(pid != null) statement.setInt(index++, pid)
            statement.setInt(index++, limit)
            statement.setInt(index, skip)
            val result = statement.executeQuery()

            val sessions = mutableListOf<Session>()

            while(result.next()) {
                val statement2 = it.prepareStatement(
                    "SELECT player_id FROM SessionPlayer WHERE session_id = ?"
                )
                statement2.setInt(1, result.getInt("sid"))
                val result2 = statement2.executeQuery()

                val players = mutableListOf<Int>()
                while(result2.next()) {
                    players.add(result2.getInt("player_id"))
                }

                sessions.add(
                    Session(
                        id = result.getInt("sid"),
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

    override fun checkSessionExists(sid: Int): Boolean {
        dataSource.connection.use {
            val statement = it.prepareStatement(
                "SELECT * FROM Session WHERE sid = ?"
            )
            statement.setInt(1, sid)

            val result = statement.executeQuery()
            return result.next()
        }
    }
}