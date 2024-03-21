package pt.isel.ls.services

import pt.isel.ls.domain.Player
import pt.isel.ls.repo.interfaces.PlayersRepo
import pt.isel.ls.utils.CAPACITY_LOWER_BOUND
import pt.isel.ls.utils.CAPACITY_UPPER_BOUND
import pt.isel.ls.utils.FIRST_PLAYER_ID
import java.util.UUID.randomUUID


class PlayerServices(private val pRepo: PlayersRepo) {

    fun createPlayer(name: String, email: String, password: String): Int {
        //Verifying if the name and email are not empty and if the email length is between the bounds
        require(name.isNotEmpty()) { "Name cannot be empty" }
        require(!name.contains(" ")) { "Name cannot contain spaces" }
        require(name.length in CAPACITY_LOWER_BOUND..CAPACITY_UPPER_BOUND) {
            "Name length must be between $CAPACITY_LOWER_BOUND and $CAPACITY_UPPER_BOUND"
        }
        val emailRegex = "^[A-Za-z0-9+_.-]{6,20}@[A-Za-z0-9.-]{9,20}$"
        require(email.matches(emailRegex.toRegex())) { "Invalid email" }

        //Creating a random token
        val token = randomUUID().toString()

        //Creating the player
        return pRepo.createPlayer(name, email, token, password)
    }

    fun getPlayer(pid: Int): Player {
        require(pid >= FIRST_PLAYER_ID) { "Player id must be greater or equal to $FIRST_PLAYER_ID" }
        return pRepo.getPlayer(pid)
    }

    fun getPlayerIdByToken(token: String): Int {
        return pRepo.getPlayerIdByToken(token)
    }
}