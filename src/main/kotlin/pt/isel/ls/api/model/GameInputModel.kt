package pt.isel.ls.api.model

import kotlinx.serialization.Serializable


@Serializable
data class GameInputModel(val name: String, val developer: String, val genres: List<String>)