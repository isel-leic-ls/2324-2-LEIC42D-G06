package pt.isel.ls.repo.mem

import pt.isel.ls.domain.Game
import pt.isel.ls.AppException
import pt.isel.ls.repo.interfaces.GamesRepo
import pt.isel.ls.utils.FIRST_GAME_ID
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger


class MemGamesRepo : GamesRepo {
    private val games = ConcurrentLinkedQueue<Game>()
    private val currentId = AtomicInteger(FIRST_GAME_ID)

    override fun checkGameExistsById(gid: Int): Boolean =
        games.any { it.id == gid }

    override fun checkGameExistsByName(name: String): Boolean =
        games.any { it.name.uppercase() == name.uppercase() }

    override fun insert(name: String, developer: String, genres: List<String>): Int {
        val gameId = currentId.getAndIncrement()
        games.add(Game(gameId, name, developer, genres))
        return gameId
    }

    override fun getGameById(gid: Int): Game =
        games.find { it.id == gid } ?: throw AppException.GameNotFound("Game not found with id $gid")

    override fun getGameByName(name: String): Game =
        games.find { it.name == name } ?: throw AppException.GameNotFound("Game not found with name $name")

    override fun getListOfGames(genres: List<String>, developer: String, limit: Int, skip: Int): List<Game> {
        val genresUpperCased = genres.map { it.uppercase() } //to make the search case-insensitive
        val devUpperCased = developer.uppercase() //to make the search case-insensitive
        val fullList = games.filter {
            it.dev.uppercase() == devUpperCased || it.genres.any { g -> g.uppercase() in genresUpperCased }
        }

        val fullListSize = fullList.size
        val lastIdx = if (skip + limit > fullListSize) fullListSize else skip + limit
        val firstIdx = if (skip > fullListSize) fullListSize else skip

        return fullList.subList(firstIdx, lastIdx)
    }
}