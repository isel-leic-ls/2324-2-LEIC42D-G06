package pt.isel.ls.repo

import pt.isel.ls.domain.Game

interface GamesRepo {
    fun createGame(name : String, developer : String, genres : Array<String>) : Int
    fun getGame(gid : Int) : Game
    fun getListOfGames(genres : Array<String>, developer : String) : List<Game>
}