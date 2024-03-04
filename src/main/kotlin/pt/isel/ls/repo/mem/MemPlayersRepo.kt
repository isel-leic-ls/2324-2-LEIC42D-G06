package pt.isel.ls.repo.mem

import pt.isel.ls.domain.Player
import pt.isel.ls.repo.interfaces.PlayersRepo
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class MemGamesRepo : PlayersRepo {
    val players = mutableListOf<Player>()
    val monitor = ReentrantLock()
    override fun createPlayer(name: String, email: String): Int {
        monitor.withLock {
            //In case there's already a player registered
            val id = if (players.isNotEmpty()) {
                val lastId = players.last().id
                players.add(Player(lastId + 1, name, email))
                lastId + 1
            }
            //In case there's no player registered
            else {
                players.add(Player(1, name, email))
                1
            }
            return id
        }
    }

    override fun getPlayer(pid: Int): Player? {
        monitor.withLock {
            return players.find { player -> player.id == pid }
        }
    }

}