package pt.isel.ls.repo.mem

import pt.isel.ls.domain.Game
import pt.isel.ls.repo.Exceptions
import pt.isel.ls.repo.interfaces.GamesRepo
import pt.isel.ls.utils.INITIAL_GAME_ID
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger


class MemGamesRepo : GamesRepo {
    private val games = ConcurrentLinkedQueue<Game>()
    private val currentId = AtomicInteger(INITIAL_GAME_ID)

    override fun insert(name: String, developer: String, genres: List<String>): Int {
        val gameId = currentId.getAndIncrement()
        games.add(Game(gameId, name, developer, genres))
        return gameId
    }

    override fun getGameById(gid: Int): Game =
        games.find { it.id == gid } ?: throw Exceptions.GameNotFound("Game not found with id $gid")

    override fun getGameByName(name: String): Game =
        games.find { it.name == name } ?: throw Exceptions.GameNotFound("Game not found with name $name")

    override fun getListOfGames(genres: List<String>, developer: String, limit: Int, skip: Int): List<Game> {
        check(skip >= 0) { "Skip must be a non-negative number" }
        check(limit > 0) { "Limit must be a positive number" }

        val fullList = games.filter { it.dev == developer || it.genres.any { g -> genres.contains(g) } }
        val fullListSize = fullList.size
        val lastIdx = if (skip + limit > fullListSize) fullListSize else skip + limit
        val firstIdx = if (skip > fullListSize) fullListSize else skip

        return fullList.subList(firstIdx, lastIdx)
    }
}