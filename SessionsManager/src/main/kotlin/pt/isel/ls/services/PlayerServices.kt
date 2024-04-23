package pt.isel.ls.services

import pt.isel.ls.AppException
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.validatePlayerCredentials
import pt.isel.ls.repo.interfaces.PlayersRepo
import pt.isel.ls.utils.*
import java.util.UUID.randomUUID


class PlayerServices(private val pRepo: PlayersRepo) {

    fun createPlayer(name: String, email: String, password: String): Pair<String, Int> {
        //Verifying if the name and email are not empty and if the email length is between the bounds
        validatePlayerCredentials(name, email, password)

        if (pRepo.checkPlayerExistsByName(name))
            throw AppException.PlayerAlreadyExists("Name $name already exists")

        //Checking if the email already exists
        if (pRepo.checkPlayerExistsByEmail(email))
            throw AppException.PlayerAlreadyExists("Email $email already exists")

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

    fun getPlayerIdByToken(token: String) = pRepo.getPlayerIdByToken(token)

    fun getPlayerByEmail(email: String) = pRepo.getPlayerByEmail(email)
}