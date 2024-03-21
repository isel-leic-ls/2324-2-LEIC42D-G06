package pt.isel.ls.services

import pt.isel.ls.domain.Player
import pt.isel.ls.repo.interfaces.PlayersRepo
import pt.isel.ls.utils.MIN_LENGTH
import pt.isel.ls.utils.MAX_LENGTH
import pt.isel.ls.utils.FIRST_PLAYER_ID
import java.util.UUID.randomUUID


class PlayerServices(private val pRepo: PlayersRepo) {

    fun createPlayer(name: String, email: String, password: String): Pair<String, Int> {
        //Verifying if the name and email are not empty and if the email length is between the bounds
        require(name.isNotEmpty()) { "Name cannot be empty" }
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(name.length in MIN_LENGTH..MAX_LENGTH) {
            "Name length must be between $MIN_LENGTH and $MAX_LENGTH"
        }
        require(password.isNotEmpty()) { "Password cannot be empty" }
        require(password.isNotBlank()) { "Password cannot be blank" }
        require(password.length in MIN_LENGTH..MAX_LENGTH ) {
            "Password length must be between $MIN_LENGTH and $MAX_LENGTH"
        }
        val emailRegex = "^[A-Za-z0-9+_.-]{6,20}@[A-Za-z0-9.-]{9,20}$"
        require(email.matches(emailRegex.toRegex())) { "Invalid email" }

        //Checking if the email already exists
        require(!pRepo.checkPlayerExistsByEmail(email)) { "Email already exists" }

        //Creating a random token
        val token = randomUUID().toString()

        //Creating the player
        val pid = pRepo.createPlayer(name, email, token, password)
        return token to pid
    }

    fun getPlayer(pid: Int): Player {
        require(pid >= FIRST_PLAYER_ID) { "Player id must be greater or equal to $FIRST_PLAYER_ID" }
        return pRepo.getPlayer(pid)
    }
}