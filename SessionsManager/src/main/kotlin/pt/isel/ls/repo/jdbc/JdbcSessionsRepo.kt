package pt.isel.ls.repo.jdbc

import SessionRepo
import pt.isel.ls.AppException
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.Session1
import pt.isel.ls.domain.SessionDTO

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
            if (!result.next()) throw AppException.SQLException("Session not inserted")
            val sid = result.getInt(1)

            val query2 = "INSERT INTO SessionPlayer(session_id, player_id) VALUES (?, ?)"
            it.prepareStatement(query2)
                .bindParameters(sid, dto.players[0])
                .executeUpdate()

            return sid

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
            val query = """
                SELECT s.sid, s.capacity, s.session_date, s.game_id, s.closed, sp.player_id 
                FROM Session s 
                LEFT JOIN SessionPlayer sp ON s.sid = sp.session_id 
                WHERE s.sid = ?
            """.trimIndent()
            val result = it.prepareStatement(query)
                .bindParameters(sid)
                .executeQuery()

            if (!result.next()) throw AppException.SessionNotFound("Session $sid does not exist")
            val capacity = result.getInt("capacity")
            val date = result.getString("session_date")
            val game = result.getInt("game_id")
            val closed = result.getBoolean("closed")
            val players = result.toPlayersList(sid)
            return Session(id = sid, capacity = capacity, date = date, game = game, closed = closed, players = players)
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

            val query = buildString {
                append("SELECT DISTINCT s.sid, s.capacity, s.session_date, s.game_id, s.closed, sp.player_id ")
                append("FROM Session s ")
                append("LEFT JOIN SessionPlayer sp ON s.sid = sp.session_id ")
                append("WHERE 1=1")
                if (gid != null) append(" AND s.game_id = ?")
                if (date != null) append(" AND DATE(s.session_date) = CAST(? AS DATE)")
                if (state != null) append(" AND s.closed = ?")
                if (pid != null) append(" AND sp.player_id = ?")
                append(" ORDER BY s.sid")
            }


            val result = it.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
                .bindParameters(gid, date, state, pid)
                .executeQuery()

            val sessions = mutableListOf<Session>()

            while (result.next()) {
                val capacity = result.getInt("capacity")
                val sDate = result.getString("session_date")
                val game = result.getInt("game_id")
                val closed = result.getBoolean("closed")
                val sid = result.getInt("sid")
                val players = result.toPlayersList(sid)
                // go back one row to align with the players list
                result.previous()

                sessions.add(
                    Session(
                        id = sid,
                        capacity = capacity,
                        date = sDate,
                        game = game,
                        closed = closed,
                        players = players
                    )
                )
            }

            return sessions.drop(skip).take(limit) to sessions.size
        }
    }

    override fun getOpenSessions(
        skip: Int, limit: Int
    ): List<Session1> {
        val query = "SELECT * FROM session WHERE closed = ?"
        dataSource.connection.use { connection ->
            val preparedStatement = connection.prepareStatement(query)
            preparedStatement.setBoolean(1, false)
            val result = preparedStatement.executeQuery()

            val sessions = mutableListOf<Session1>()

            while (result.next()) {
                val capacity = result.getInt("capacity")
                val sid = result.getInt("sid")
                val sDate = result.getString("session_date")
                val game = result.getInt("game_id")
                val closed = result.getBoolean("closed")


                sessions.add(
                    Session1(
                        id = sid,
                        capacity = capacity,
                        date = sDate,
                        game = game,
                        closed = closed,
                    )

                )
            }
            return sessions.drop(skip).take(limit
            )
        }
    }

    private fun ResultSet.toPlayersList(sid: Int): List<Int> = buildList {
        do {
            add(getInt("player_id"))
        } while (next() && getInt("sid") == sid)
    }

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