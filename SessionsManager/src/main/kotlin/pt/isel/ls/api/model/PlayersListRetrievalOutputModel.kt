package pt.isel.ls.api.model

import kotlinx.serialization.Serializable
import pt.isel.ls.domain.PlayerDetails


@Serializable
data class PlayersListRetrievalOutputModel(val players: List<PlayerDetails>, val total: Int)