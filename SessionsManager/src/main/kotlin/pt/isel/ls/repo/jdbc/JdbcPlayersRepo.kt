package pt.isel.ls.repo.jdbc

import pt.isel.ls.AppException
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player
import pt.isel.ls.repo.interfaces.PlayersRepo
import java.sql.Statement
import javax.sql.DataSource

class JdbcPlayersRepo(private val dataSource: DataSource) : PlayersRepo {
    override fun createPlayer(name: String, email: String, token: String, password: String): Int {
        dataSource.connection.use {
            val stmt = it.prepareStatement(
                "INSERT INTO Player(name, email, token, password) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            )

            stmt.setString(1, name)
            stmt.setString(2, email)
            stmt.setString(3, token)
            stmt.setString(4, password)
            stmt.executeUpdate()

            val rs = stmt.generatedKeys
            if (rs.next()) return rs.getInt(1)
            else throw AppException.SQLException("Player not inserted")

        }
    }

    override fun getPlayer(pid: Int): Player {
        dataSource.connection.use {
            val stmt = it.prepareStatement("SELECT * FROM player WHERE pid = ?")
            stmt.setInt(1, pid)
            val rs = stmt.executeQuery()
            if (!rs.next()) throw AppException.PlayerNotFound("Player $pid does not exist")

            return Player(
                rs.getInt("pid"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("token"),
                rs.getString("password")
            )
        }
    }

    override fun checkPlayerExistsByEmail(email: String): Boolean {
        dataSource.connection.use {
            val stmt = it.prepareStatement("SELECT * FROM player WHERE email = ?")
            stmt.setString(1, email)
            val rs = stmt.executeQuery()
            return rs.next()
        }
    }

    override fun checkPlayerExistsByName(name: String): Boolean {
        dataSource.connection.use {
            val stmt = it.prepareStatement("SELECT * FROM player WHERE name = ?")
            stmt.setString(1, name)
            val rs = stmt.executeQuery()
            return rs.next()
        }
    }

    override fun getPlayerIdByToken(token: String): Int {
        dataSource.connection.use {
            val result = it.prepareStatement("SELECT pid FROM player WHERE token = ?")
                .bindParameters(token)
                .executeQuery()
            if (!result.next()) throw AppException.PlayerNotFound("Player $token does not exist")
            return result.getInt("pid")
        }
    }

    override fun getListOfPlayedGames(pid: Int, limit: Int, skip: Int): Pair<List<Game>, Int> {
        dataSource.connection.use {
            val query = buildString {
                append("SELECT DISTINCT g.* ")
                append("FROM game g ")
                append("INNER JOIN session s ON g.gid = s.game_id ")
                append("INNER JOIN sessionplayer sp ON s.sid = sp.session_id ")
                append("WHERE sp.player_id = ? AND TO_DATE(s.session_date , 'YYYY-MM-DD HH24:MI:SS') < CURRENT_DATE;")
            }

            val result = it.prepareStatement(query)
                .bindParameters(pid)
                .executeQuery()

            val games = mutableListOf<Game>()
            while (result.next()) {
                games.add(
                    Game(
                        result.getInt("gid"),
                        result.getString("name"),
                        result.getString("developer"),
                        result.getString("genres").drop(1).dropLast(1).split(",")
                    )
                )
            }
            return games.drop(skip).take(limit) to games.size
        }
    }
}