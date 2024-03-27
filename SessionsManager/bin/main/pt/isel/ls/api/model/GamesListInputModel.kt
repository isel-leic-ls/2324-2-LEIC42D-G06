package pt.isel.ls.api.model

import kotlinx.serialization.Serializable


@Serializable
data class GamesListInputModel(val genres: List<String>, val developer: String)