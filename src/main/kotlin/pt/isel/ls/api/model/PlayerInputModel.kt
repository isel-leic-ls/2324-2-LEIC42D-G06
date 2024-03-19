package pt.isel.ls.api.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerInputModel(val name: String, val email: String, val token: String)