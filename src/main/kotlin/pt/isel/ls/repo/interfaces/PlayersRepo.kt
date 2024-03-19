package pt.isel.ls.repo.interfaces

import pt.isel.ls.domain.Player

interface PlayersRepo {
    fun createPlayer(name: String, email: String, token: String): Int
    fun getPlayer(pid: Int): Player


    fun getPlayerIdByToken(token: String): Int

}