package pt.isel.ls.repo.mem

import pt.isel.ls.domain.Player
import pt.isel.ls.AppException
import pt.isel.ls.repo.interfaces.PlayersRepo
import pt.isel.ls.utils.FIRST_PLAYER_ID
import java.util.concurrent.ConcurrentLinkedQueue


class MemPlayersRepo : PlayersRepo {
    private val players = ConcurrentLinkedQueue<Player>()

    override fun createPlayer(name: String, email: String, token: String, password: String): Int {
        val id = if (players.isNotEmpty()) players.last().id + 1
        else FIRST_PLAYER_ID
        players.add(Player(id, name, email, token, password))
        return id
    }

    override fun getPlayer(pid: Int) = players.find { player -> player.id == pid } ?: throw AppException.PlayerNotFound("Player not found with id $pid")
    override fun checkPlayerExistsByEmail(email: String): Boolean = players.any { it.email == email }
    override fun checkPlayerExistsByName(name: String): Boolean = players.any { it.name == name }

    override fun getPlayerIdByToken(token: String) = players.find { it.token == token }?.id
        ?: throw AppException.PlayerNotFound("Player not found with token $token")
}