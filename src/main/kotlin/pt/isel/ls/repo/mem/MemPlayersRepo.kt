package pt.isel.ls.repo.mem

import pt.isel.ls.domain.Player
import pt.isel.ls.repo.DomainException
import pt.isel.ls.repo.interfaces.PlayersRepo
import java.util.UUID.randomUUID
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class MemPlayersRepo : PlayersRepo {
    private val players = mutableListOf<Player>()
    private val monitor = ReentrantLock()
    override fun createPlayer(name: String, email: String): Int {
        monitor.withLock {
            val token = randomUUID().toString()
            //In case there's already a player registered
            val id = if (players.isNotEmpty()) {
                val lastId = players.last().id
                players.add(Player(lastId + 1, name, email, token))
                lastId + 1
            }
            //In case there's no player registered
            else {
                players.add(Player(1, name, email, token))
                1
            }
            return id
        }
    }

    override fun getPlayer(pid: Int): Player? {
        monitor.withLock {
            return findPlayerId(pid)
        }
    }

    override fun getPlayerToken(pid: Int): String? {
        monitor.withLock {
            return findPlayerId(pid)?.token
        }
    }

    override fun getPlayerIdByToken(token: String): Int {
        monitor.withLock {
            return players.find { it.token == token }?.id ?: throw DomainException.PlayerNotFound("Player with token $token does not exist")
        }
    }

    private fun findPlayerId(pid: Int): Player? {
        monitor.withLock {
            return players.find { player -> player.id == pid }
        }
    }

}