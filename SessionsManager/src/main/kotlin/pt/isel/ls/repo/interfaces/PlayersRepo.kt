package pt.isel.ls.repo.interfaces

import pt.isel.ls.domain.Game
import pt.isel.ls.domain.Player

interface PlayersRepo {
    fun createPlayer(name: String, email: String, token: String, password: String): Int
    fun getPlayer(pid: Int): Player
    fun checkPlayerExistsByEmail(email: String): Boolean
    fun checkPlayerExistsByName(name: String): Boolean
    fun getPlayerIdByToken(token: String): Int

    fun getListOfPlayedGames(pid: Int, limit: Int, skip: Int): Pair<List<Game>, Int>

}