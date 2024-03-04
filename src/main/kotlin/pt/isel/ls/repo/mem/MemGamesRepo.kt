package pt.isel.ls.repo.mem

import pt.isel.ls.domain.Game
import pt.isel.ls.repo.Exceptions
import pt.isel.ls.repo.interfaces.GamesRepo
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger


const val INITIAL_GAME_ID = 100

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

    override fun getListOfGames(genres: List<String>, developer: String): List<Game> =
        games.filter { it.dev == developer || it.genres.any { g -> genres.contains(g) } }
}