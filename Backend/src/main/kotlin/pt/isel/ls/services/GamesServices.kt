package pt.isel.ls.services

import pt.isel.ls.domain.Game
import pt.isel.ls.repo.DomainException
import pt.isel.ls.repo.interfaces.GamesRepo
import pt.isel.ls.repo.interfaces.PlayersRepo
import pt.isel.ls.utils.FIRST_GAME_ID
import pt.isel.ls.utils.LIMIT_DEFAULT
import pt.isel.ls.utils.SKIP_DEFAULT


class GamesServices(private val gRepo: GamesRepo, private val pRepo: PlayersRepo) {
    fun createGame(token: String, name: String, developer: String, genres: List<String>): Int {
        pRepo.getPlayerIdByToken(token) ?: throw DomainException.PlayerNotFound("Player not found with token $token")

        if (gRepo.checkGameExistsByName(name))
            throw DomainException.GameAlreadyExists("Game $name already exists")

        if (name.isBlank() || developer.isBlank() || genres.isEmpty() || genres.any { it.isBlank() })
            throw DomainException.BadRequestCreateGame("Invalid game data")

        return gRepo.insert(name, developer, genres)
    }

    fun getDetailsOfGameById(id: Int): Game {
        require(id >= FIRST_GAME_ID) { "Game id must be greater or equal to $FIRST_GAME_ID" }
        return gRepo.getGameById(id)
    }

    fun getDetailsOfGameByName(name: String) = gRepo.getGameByName(name)

    fun getListOfGames(
        genres: List<String>, developer: String, limit: Int = LIMIT_DEFAULT, skip: Int = SKIP_DEFAULT
    ): List<Game> {
        check(limit > 0) { "Limit must be a positive number" }
        check(skip >= 0) { "Skip must be a non-negative number" }

        return gRepo.getListOfGames(genres, developer, limit, skip)
    }
}