package pt.isel.ls.repo.jdbc

import SessionRepo
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionDTO
import pt.isel.ls.utils.toSqlTimestamp
import java.time.LocalDateTime
import java.util.*
import javax.sql.DataSource
class JdbcSessionsRepo(private val dataSource : DataSource) : SessionRepo {
    override fun createSession(sessionDTO: SessionDTO): Int {
        dataSource.connection.use {
            val statement = it.prepareStatement(
                "INSERT INTO session(capacity, date, game, closed) VALUES (?, ?, ?, ?)"
            )

            statement.setInt(1, sessionDTO.capacity)
            statement.setTimestamp(2, sessionDTO.date.toSqlTimestamp())
            statement.setInt(3, sessionDTO.game)
            statement.setBoolean(4, sessionDTO.closed)

            val result = statement.executeUpdate()
            if (result == 0) throw Exception("Session not inserted")
            val sid = statement.generatedKeys.getInt(1)

            val statement2 = it.prepareStatement(
                "INSERT INTO session_player(session, player) VALUES (?, ?)"
            )
            statement2.setInt(1, sid)
            statement2.setInt(2, sessionDTO.players[0])

            val result2 = statement2.executeUpdate()
            if (result2 == 0) throw Exception("Session not inserted")

            return sid
        }
    }

    override fun addPlayerToSession(updatedSession : Session) {
        dataSource.connection.use {
            val statement = it.prepareStatement(
                "INSERT INTO session_player(session, player) VALUES (?, ?)"
            )
            statement.setInt(1, updatedSession.id)
            statement.setInt(2, updatedSession.players.last())
            val result = statement.executeUpdate()

            if (result == 0) throw Exception("Player not inserted")
            return
        }
    }

    override fun getSession(sid: Int): Session {
        dataSource.connection.use {
            val statement = it.prepareStatement(
                "SELECT * FROM session WHERE sid = ?"
            )
            statement.setInt(1, sid)
            val result = statement.executeQuery()

            val statement2 = it.prepareStatement(
                "SELECT player FROM session_player WHERE session = ?"
            )
            statement2.setInt(1, sid)
            val result2 = statement2.executeQuery()

            val players = mutableListOf<Int>()
            while(result2.next()) {
                players.add(result2.getInt("player"))
            }

            if (!result.next()) throw Exception("Session $sid not found")
            return Session(
                id = sid,
                capacity = result.getInt("capacity"),
                date = result.getTimestamp("date").toLocalDateTime(),
                game = result.getInt("game"),
                closed = result.getInt("capacity") == players.size,
                players = players
            )
        }
    }

    override fun getListOfSessions(
        gid: Int,
        date: LocalDateTime?,
        state: Boolean?,
        pid: Int?,
        skip: Int,
        limit: Int
    ): List<Session> {
        dataSource.connection.use {
            TODO()
        }
    }

    override fun checkSessionExists(sid: Int): Boolean {
        dataSource.connection.use {
            val statement = it.prepareStatement(
                "SELECT * FROM session WHERE sid = ?"
            )
            statement.setInt(1, sid)

            val result = statement.executeQuery()
            return result.next()
        }
    }
}