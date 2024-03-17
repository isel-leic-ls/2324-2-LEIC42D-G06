package pt.isel.ls.api.model

import kotlinx.serialization.Serializable
import pt.isel.ls.domain.Game


@Serializable
data class GamesListOutputModel(val games: List<Game>)