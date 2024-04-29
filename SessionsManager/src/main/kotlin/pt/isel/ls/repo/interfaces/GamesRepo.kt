package pt.isel.ls.repo.interfaces

import pt.isel.ls.domain.Game


interface GamesRepo {
    /** checks if a game with the given id exists */
    fun checkGameExistsById(gid: Int): Boolean

    /** checks if a game with the given name exists (case-insensitive) */
    fun checkGameExistsByName(name: String): Boolean

    /** returns the id of the inserted game */
    fun insert(name: String, developer: String, genres: List<String>): Int

    /** get details of a game by its id */
    fun getGameById(gid: Int): Game

    /** get details of a game by its name */
    fun getGameByName(name: String): Game

    /** get a list of games search by the given name (can be partial)
     *  this list is case-insensitive and limited by the limit parameter and skips the first skip games */
    fun getGamesByName(name: String, limit: Int, skip: Int): Pair<List<Game>, Int>

    /** get a list made by the list of games of given genre(s) plus the list of games of the given developer;
     *  this list is limited by the limit parameter and skips the first skip games (case-insensitive) */
    fun getListOfGames(genres: List<String>, developer: String, limit: Int, skip: Int): Pair<List<Game>, Int>
}