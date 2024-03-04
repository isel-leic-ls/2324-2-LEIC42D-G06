package pt.isel.ls.repo.mem

import pt.isel.ls.domain.Game
import pt.isel.ls.repo.interfaces.GamesRepo


/*class MemGamesRepo : GamesRepo {
    val games = mutableListOf<Game>()

    override fun insert(name: String, developer: String, genres: List<String>): Int {
        val gameId = games.size
        games.add(Game(gameId, name, developer, genres))
        return gameId
    }

    override fun getGameById(gid: Int): Game = games[gid]

    override fun getGameByName(name: String): Game =
        games.find { it.name == name } ?: throw NoSuchElementException("Game not found")

    override fun getListOfGames(genres: List<String>, developer: String): List<Game> =
        games.filter { it.dev == developer || it.genres.any { g -> genres.contains(g) } }
}*/