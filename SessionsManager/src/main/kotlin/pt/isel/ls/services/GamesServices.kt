package pt.isel.ls.services

import pt.isel.ls.domain.Game
import pt.isel.ls.AppException
import pt.isel.ls.repo.interfaces.GamesRepo
import pt.isel.ls.repo.interfaces.PlayersRepo
import pt.isel.ls.utils.*


class GamesServices(private val gRepo: GamesRepo, private val pRepo: PlayersRepo) {
    fun createGame(token: String, name: String, developer: String, genres: List<String>): Int {
        pRepo.getPlayerIdByToken(token)

        if (gRepo.checkGameExistsByName(name))
            throw AppException.GameAlreadyExists("Game $name already exists")

        if (
            name.isBlank()
            || developer.isBlank() || genres.isEmpty() || genres.any { it.isBlank() }
            || genres.groupingBy { it }.eachCount().any { it.value > 1 }
            || name.length > MAX_GAME_LENGTH
            || developer.length > MAX_DEVELOPER_LENGTH
            || genres.any { it.length > MAX_GENRE_LENGTH }
        )
            throw AppException.BadRequestCreateGame("Invalid game data")

        return gRepo.insert(name, developer, genres)
    }

    fun getDetailsOfGameById(id: Int): Game {
        require(id >= FIRST_GAME_ID) { "Game id must be greater or equal to $FIRST_GAME_ID" }
        return gRepo.getGameById(id)
    }

    fun getDetailsOfGameByName(name: String) = gRepo.getGameByName(name)

    fun getListOfGamesByName(
        name: String, limit: Int = LIMIT_DEFAULT, skip: Int = SKIP_DEFAULT
    ): Pair<List<Game>, Int> {
        if (name.isBlank())
            throw IllegalArgumentException("Invalid name input")

        require(limit > 0) { "Limit must be a positive number" }
        require(skip >= 0) { "Skip must be a non-negative number" }

        return gRepo.getGamesByName(name, limit, skip)
    }

    fun getListOfGames(
        genres: List<String>, developer: String, limit: Int = LIMIT_DEFAULT, skip: Int = SKIP_DEFAULT
    ): Pair<List<Game>, Int> {
        require(limit > 0) { "Limit must be a positive number" }
        require(skip >= 0) { "Skip must be a non-negative number" }

        return gRepo.getListOfGames(genres.map { it.trim() }, developer.trim(), limit, skip)
    }
}