package pt.isel.ls.repo.interfaces

import pt.isel.ls.domain.Player
import pt.isel.ls.domain.PlayerDetails

interface PlayersRepo {
    fun createPlayer(name: String, email: String, token: String, password: String): Int
    fun getPlayer(pid: Int): Player
    fun checkPlayerExistsByEmail(email: String): Boolean
    fun checkPlayerExistsByName(name: String): Boolean
    fun getPlayerIdByToken(token: String): Int
    fun getPlayerByName(name: String): Player
    fun getPlayersByUsername(username: String, skip:Int,limit:Int):Pair<List<PlayerDetails>, Int>
}