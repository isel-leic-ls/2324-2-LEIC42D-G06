package pt.isel.ls.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionOutputModel(val sid : String)