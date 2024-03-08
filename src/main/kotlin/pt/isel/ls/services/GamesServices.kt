package pt.isel.ls.services

import pt.isel.ls.domain.Game
import pt.isel.ls.repo.DomainException
import pt.isel.ls.repo.interfaces.GamesRepo
import pt.isel.ls.utils.LIMIT_DEFAULT
import pt.isel.ls.utils.SKIP_DEFAULT


class GamesServices(private val gRepo: GamesRepo) {
    fun createGame(name: String, developer: String, genres: List<String>): Int {
        //TODO on WebApi check if the arguments are trimmed

        if (gRepo.checkGameExistsByName(name))
            throw DomainException.GameAlreadyExists("Game $name already exists")

        if (name.isBlank() || developer.isBlank() || genres.isEmpty() || genres.any { it.isBlank() })
            throw DomainException.BadRequestCreateGame("Invalid game data")

        return gRepo.insert(name, developer, genres)
    }

    fun getDetailsOfGameById(gid: Int) = gRepo.getGameById(gid)

    fun getDetailsOfGameByName(name: String) = gRepo.getGameByName(name)

    fun getListOfGames(
        genres: List<String>, developer: String, limit: Int = LIMIT_DEFAULT, skip: Int = SKIP_DEFAULT
    ): List<Game> {
        check(limit > 0) { "Limit must be a positive number" }
        check(skip >= 0) { "Skip must be a non-negative number" }

        return gRepo.getListOfGames(genres, developer, limit, skip)
    }
}