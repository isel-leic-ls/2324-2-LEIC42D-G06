package pt.isel.ls.domain


data class Game(val id: Int, val name: String, val dev: String, val genres: List<String>)

data class GameDetails(val name: String, val dev: String, val genres: List<String>)