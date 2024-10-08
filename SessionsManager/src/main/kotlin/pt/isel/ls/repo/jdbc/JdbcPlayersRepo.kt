package pt.isel.ls.repo.jdbc

import pt.isel.ls.AppException
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.PlayerDetails
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

    override fun getPlayerByName(name: String): Player {
        dataSource.connection.use {
            val result = it.prepareStatement("SELECT * FROM player WHERE name = ?")
                .bindParameters(name)
                .executeQuery()

            if (!result.next()) throw AppException.PlayerNotFound("Player $name does not exist")
            return Player(
                result.getInt("pid"),
                result.getString("name"),
                result.getString("email"),
                result.getString("token"),
                result.getString("password")
            )
        }
    }

    override fun getPlayersByUsername(username: String, skip: Int, limit: Int): Pair<List<PlayerDetails>, Int> {
        dataSource.connection.use {
            val result = it.prepareStatement("SELECT * FROM player WHERE name ILIKE ?")
                .bindParameters("$username%")
                .executeQuery()

            val playerInfoList = mutableListOf<PlayerDetails>()

            while (result.next()) {
                val id = result.getInt("pid")
                val name = result.getString("name")
                val email = result.getString("email")
                playerInfoList.add(PlayerDetails(id, name, email))
            }

            return playerInfoList.drop(skip).take(limit) to playerInfoList.size
        }
    }
}