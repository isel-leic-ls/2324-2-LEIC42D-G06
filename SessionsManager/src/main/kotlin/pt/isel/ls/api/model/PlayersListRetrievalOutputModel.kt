package pt.isel.ls.api.model

import kotlinx.serialization.Serializable
import pt.isel.ls.domain.Player

@Serializable
data class PlayersListRetrievalOutputModel(val playersList: List<Player>)