package pt.isel.ls.repo.jdbc

import pt.isel.ls.AppException
import pt.isel.ls.domain.Game
import pt.isel.ls.repo.interfaces.GamesRepo
import java.sql.Statement
import javax.sql.DataSource


class JdbcGamesRepo(private val dataSource: DataSource) : GamesRepo {
    override fun checkGameExistsById(gid: Int): Boolean {
        dataSource.connection.use {
            val stmt = it.prepareStatement("SELECT * FROM game WHERE gid = ?")
            stmt.setInt(1, gid)
            val rs = stmt.executeQuery()
            return rs.next()
        }
    }

    override fun checkGameExistsByName(name: String): Boolean {
        dataSource.connection.use {
            val stmt = it.prepareStatement("SELECT * FROM game WHERE name ILIKE ?")
            stmt.setString(1, name)
            val rs = stmt.executeQuery()
            return rs.next()
        }
    }

    override fun insert(name: String, developer: String, genres: List<String>): Int {
        dataSource.connection.use {
            val genresArray = genres.toTypedArray()
            val stmt = it.prepareStatement(
                "INSERT INTO Game(name, developer, genres) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            )
            stmt.setString(1, name)
            stmt.setString(2, developer)
            stmt.setArray(3, it.createArrayOf("VARCHAR", genresArray))
            stmt.executeUpdate()

            val rs = stmt.generatedKeys
            if (rs.next()) return rs.getInt(1)
            else throw AppException.SQLException("Game not inserted")
        }
    }

    override fun getGameById(gid: Int): Game {
        dataSource.connection.use {
            val stmt = it.prepareStatement("SELECT * FROM game WHERE gid = ?")
            stmt.setInt(1, gid)
            val rs = stmt.executeQuery()
            if (!rs.next()) throw AppException.GameNotFound("Game $gid does not exist")

            return Game(
                rs.getInt("gid"),
                rs.getString("name"),
                rs.getString("developer"),
                rs.getString("genres").drop(1).dropLast(1).split(",")
            )
        }
    }

    override fun getGameByName(name: String): Game {
        dataSource.connection.use {
            val stmt = it.prepareStatement("SELECT * FROM game WHERE name ILIKE ?")
            stmt.setString(1, name)
            val rs = stmt.executeQuery()
            if (!rs.next()) throw AppException.GameNotFound("Game $name does not exist")

            return Game(
                rs.getInt("gid"),
                rs.getString("name"),
                rs.getString("developer"),
                rs.getString("genres").drop(1).dropLast(1).split(",")
            )
        }
    }

    override fun getListOfGames(
        genres: List<String>, developer: String, limit: Int, skip: Int
    ): Pair<List<Game>,Int> {
        dataSource.connection.use {
            val stmt = it.prepareStatement(
                "SELECT * FROM game WHERE developer ILIKE ? OR " +
                        "EXISTS (SELECT 1 FROM UNNEST(genres) AS genre WHERE genre ILIKE ANY(?)) " +
                        "ORDER BY gid"
            )
            stmt.setString(1, developer)
            stmt.setArray(2, it.createArrayOf("VARCHAR", genres.toTypedArray()))
            val rs = stmt.executeQuery()

            val games = mutableListOf<Game>()
            while (rs.next()) {
                games.add(
                    Game(
                        rs.getInt("gid"),
                        rs.getString("name"),
                        rs.getString("developer"),
                        rs.getString("genres").drop(1).dropLast(1).split(",")
                    )
                )
            }
            return games.drop(skip).take(limit) to games.size
        }
    }
}