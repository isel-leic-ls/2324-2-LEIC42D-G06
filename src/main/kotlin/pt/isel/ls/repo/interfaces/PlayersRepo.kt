package pt.isel.ls.repo.interfaces

import pt.isel.ls.domain.Player

interface PlayersRepo {
    fun createPlayer(name:String, email:String): Int
    fun getPlayer(pid : Int) : Player?
    fun getPlayerToken(pid : Int) : String?

    fun getPlayerIdByToken(token : String) : Int

}