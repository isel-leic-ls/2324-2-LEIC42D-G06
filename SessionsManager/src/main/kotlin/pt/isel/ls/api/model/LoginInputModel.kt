package pt.isel.ls.api.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginInputModel(val name: String, val password: String)
