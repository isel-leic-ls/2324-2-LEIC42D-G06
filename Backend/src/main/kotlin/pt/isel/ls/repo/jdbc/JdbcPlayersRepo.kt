package pt.isel.ls.repo.jdbc

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
            if (rs.next()){
                return rs.getInt(1)
            }
            else {
                throw Exception("Player not inserted")

            }
        }
    }

    override fun getPlayer(pid: Int): Player {
        val stmt = dataSource.connection.prepareStatement("SELECT * FROM player WHERE pid = ?")
        stmt.setInt(1, pid)
        val rs = stmt.executeQuery()
        rs.next()

        return Player(
            rs.getInt("pid"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("token"),
            rs.getString("password")
        )
    }

    override fun getPlayerIdByToken(token: String): Int {
        val stmt = dataSource.connection.prepareStatement("SELECT pid FROM player WHERE token = ?")
        stmt.setString(1, token)
        val rs = stmt.executeQuery()
        rs.next()

        return rs.getInt("pid")
    }
}