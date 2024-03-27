package pt.isel.ls.api.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerRetrievalOutputModel(val id : Int, val name : String, val email : String)