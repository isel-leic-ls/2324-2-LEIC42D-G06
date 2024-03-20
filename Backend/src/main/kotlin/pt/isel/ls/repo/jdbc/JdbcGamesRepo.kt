package pt.isel.ls.repo.jdbc

import pt.isel.ls.domain.Game
import pt.isel.ls.repo.interfaces.GamesRepo
import java.sql.Connection
import java.sql.Statement
import javax.sql.DataSource


class JdbcGamesRepo(private val dataSource: DataSource) : GamesRepo {
    override fun checkGameExistsById(gid: Int): Boolean {
        val stmt = dataSource.connection.prepareStatement("SELECT * FROM game WHERE gid = ?")
        stmt.setInt(1, gid)
        val rs = stmt.executeQuery()
        return rs.next()
    }

    override fun checkGameExistsByName(name: String): Boolean {
        val stmt = dataSource.connection.prepareStatement("SELECT * FROM game WHERE name ILIKE ?")
        stmt.setString(1, name)
        val rs = stmt.executeQuery()
        return rs.next()
    }

    override fun insert(name: String, developer: String, genres: List<String>): Int {
        val genresArray = genres.toTypedArray()
        val stmt = dataSource.connection.prepareStatement(
            "INSERT INTO Game(name, developer, genres) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )
        stmt.setString(1, name)
        stmt.setString(2, developer)
        stmt.setArray(3, dataSource.connection.createArrayOf("VARCHAR", genresArray))
        stmt.executeUpdate()

        val rs = stmt.generatedKeys
        if (rs.next()) return rs.getInt(1)
        else throw Exception("Game not inserted")
    }

    override fun getGameById(gid: Int): Game {
        val stmt = dataSource.connection.prepareStatement("SELECT * FROM game WHERE gid = ?")
        stmt.setInt(1, gid)
        val rs = stmt.executeQuery()
        rs.next()

        return Game(
            rs.getInt("gid"),
            rs.getString("name"),
            rs.getString("developer"),
            rs.getString("genres").drop(1).dropLast(1).split(",")
        )
    }

    override fun getGameByName(name: String): Game {
        val stmt = dataSource.connection.prepareStatement("SELECT * FROM game WHERE name ILIKE ?")
        stmt.setString(1, name)
        val rs = stmt.executeQuery()
        rs.next()

        return Game(
            rs.getInt("gid"),
            rs.getString("name"),
            rs.getString("developer"),
            rs.getString("genres").drop(1).dropLast(1).split(",")
        )
    }

    override fun getListOfGames(genres: List<String>, developer: String, limit: Int, skip: Int): List<Game> {
        val stmt = dataSource.connection.prepareStatement(
            "SELECT * FROM game WHERE developer ILIKE ? OR array(select unnest(genres)) && ? ORDER BY gid LIMIT ? OFFSET ?"
        )
        stmt.setString(1, developer)
        stmt.setArray(2, dataSource.connection.createArrayOf("VARCHAR", genres.toTypedArray()))
        stmt.setInt(3, limit)
        stmt.setInt(4, skip)
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
        return games
    }
}