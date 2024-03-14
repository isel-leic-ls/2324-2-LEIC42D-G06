package pt.isel.ls.repo.mem

import pt.isel.ls.domain.Player
import pt.isel.ls.repo.interfaces.PlayersRepo
import java.util.concurrent.ConcurrentLinkedQueue


class MemPlayersRepo : PlayersRepo {
    private val players = ConcurrentLinkedQueue<Player>()
    override fun createPlayer(name: String, email: String, token: String): Int {
        val id = if (players.isNotEmpty()) {
            players.last().id + 1
        } else {
            1
        }
        players.add(Player(id, name, email, token))
        return id
    }

    override fun getPlayer(pid: Int) = players.find { player -> player.id == pid }


    override fun getPlayerIdByToken(token : String) = players.find { it.token == token }?.id

}

