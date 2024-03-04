package pt.isel.ls.repo.interfaces

import pt.isel.ls.domain.Game


interface GamesRepo {
    /** returns the id of the inserted game */
    fun insert(name: String, developer: String, genres: List<String>): Int

    /** get details of a game by its id */
    fun getGameById(gid: Int): Game

    /** get details of a game by its name */
    fun getGameByName(name: String): Game

    /** get a list made by the list of games of given genre(s) plus the list of games of the given developer */
    fun getListOfGames(genres: List<String>, developer: String): List<Game>
}