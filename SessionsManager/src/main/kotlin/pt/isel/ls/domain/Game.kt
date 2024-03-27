package pt.isel.ls.domain

import kotlinx.serialization.Serializable


@Serializable
data class Game(val id: Int, val name: String, val dev: String, val genres: List<String>)

data class GameDetails(val name: String, val dev: String, val genres: List<String>)