package pt.isel.ls.services

import com.toxicbakery.bcrypt.Bcrypt
import pt.isel.ls.AppException
import pt.isel.ls.domain.NAME_RANGE
import pt.isel.ls.domain.Player
import pt.isel.ls.domain.PlayerDetails
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

        val saltRounds = 10
        val hashedPassword = Bcrypt.hash(password, saltRounds).toString(Charsets.UTF_8)
        //Creating a random token
        val token = randomUUID().toString()
        //Creating the player
        val pid = pRepo.createPlayer(name, email, token, hashedPassword)
        return token to pid
    }

    fun getPlayer(pid : Int): Player {
        require(pid >= FIRST_PLAYER_ID) { "Player id must be greater or equal to $FIRST_PLAYER_ID" }
        return pRepo.getPlayer(pid)
    }

    fun getPlayerIdByToken(token: String) = pRepo.getPlayerIdByToken(token)

    fun getPlayersByUsername(username: String, skip: Int, limit: Int): Pair<List<PlayerDetails>, Int> {
        require(username.length < NAME_RANGE.last) { "Username length must be less than ${NAME_RANGE.last}" }
        require(skip >= 0) { "Skip value must be positive" }
        require(limit > 0) { "Limit value must be positive non-zero" }
        return pRepo.getPlayersByUsername(username, skip, limit)
    }

    fun login(username: String, password: String): Pair<Int, String> {
        val player = pRepo.getPlayerByName(username)
        if (!Bcrypt.verify(password,player.password.toByteArray()))
            throw IllegalArgumentException("Invalid password")
        return player.id to player.token
    }
}