package pt.isel.ls.repo.jdbc

import pt.isel.ls.domain.Game
import pt.isel.ls.repo.interfaces.GamesRepo
import java.sql.Connection
import java.sql.Statement


class JdbcGamesRepo(private val connection: Connection) : GamesRepo {
    /** returns the id of the inserted game */
    override fun insert(name: String, developer: String, genres: List<String>): Int {
        val genresArray = genres.toTypedArray()
        val stmt = connection.prepareStatement(
            "INSERT INTO Game(name, developer, genres) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )
        stmt.setString(1, name)
        stmt.setString(2, developer)
        stmt.setArray(3, connection.createArrayOf("VARCHAR", genresArray))
        stmt.executeUpdate()

        val rs = stmt.generatedKeys
        if (rs.next()) return rs.getInt(1)
        else throw Exception("Game not inserted")
    }

    override fun getGameById(gid: Int): Game {
        val stmt = connection.prepareStatement("SELECT * FROM game WHERE gid = ?")
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
        TODO()
        /*val stmt = connection.prepareStatement("SELECT * FROM game WHERE name = ?")
        stmt.setString(1, name)
        val rs = stmt.executeQuery()
        rs.next()

        return Game(
            rs.getInt("gid"), rs.getString("name"), rs.getString("dev"), rs.getString("genres").split(",")
        )*/
    }

    //do not use vars or mutable lists. I need immutability
    override fun getListOfGames(genres: List<String>, developer: String, limit: Int, skip: Int): List<Game> {
        TODO()
        /*val stmt = connection.prepareStatement("SELECT * FROM game WHERE dev = ? OR genres LIKE ?")
        stmt.setString(1, developer)
        stmt.setString(2, genres.joinToString(","))
        val rs = stmt.executeQuery()

        val games = generateSequence {
            if (rs.next()) {
                Game(
                    rs.getInt("gid"),
                    rs.getString("name"),
                    rs.getString("dev"),
                    rs.getString("genres").split(",")
                )
            } else null
        }.toList()

        return games*/
    }
}