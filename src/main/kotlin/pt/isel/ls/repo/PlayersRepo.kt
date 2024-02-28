package pt.isel.ls.repo

import pt.isel.ls.domain.Player

interface PlayersRepo {
    fun createPlayer(name:String, email:String): Int
    fun getPlayer(pid : Int) : Player
}