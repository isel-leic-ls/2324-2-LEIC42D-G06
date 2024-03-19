package pt.isel.ls.services

import pt.isel.ls.domain.Player
import pt.isel.ls.repo.interfaces.PlayersRepo
import pt.isel.ls.utils.CAPACITY_LOWER_BOUND
import pt.isel.ls.utils.CAPACITY_UPPER_BOUND
import pt.isel.ls.utils.TOKEN_LENGTH
import java.util.UUID.randomUUID

class PlayerServices(
    private val pRepo: PlayersRepo
) {
    fun createPlayer(name: String, email: String): Int{
        //Verifying if the name and email are not empty and if the email length is between the bounds
        require(name.isNotEmpty()) { "Name cannot be empty" }
        require(email.isNotEmpty()) { "Email cannot be empty" }
        require(name.length in CAPACITY_LOWER_BOUND..CAPACITY_UPPER_BOUND)
        { "Name length must be between 2 and 20" }
        require(email.length in CAPACITY_LOWER_BOUND..CAPACITY_UPPER_BOUND)
        { "Email length must be between 2 and 20" }

        //Creating a random token
        val token = randomUUID().toString()

        //Creating the player
        return pRepo.createPlayer(name, email, token)
    }

    fun getPlayer(pid: Int): Player {
        require(pid > 0) { "Player id must be greater than 0" }
        return pRepo.getPlayer(pid)
    }

    fun getPlayerIdByToken(token: String): Int {
        require(token.isNotEmpty()) { "Token cannot be empty" }
        require(token.length < TOKEN_LENGTH) { "Token length must be less than 36" }
        return pRepo.getPlayerIdByToken(token)
    }
}