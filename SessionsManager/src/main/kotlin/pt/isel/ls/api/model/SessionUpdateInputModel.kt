package pt.isel.ls.api.model

import kotlinx.serialization.Serializable

@Serializable
data class SessionUpdateInputModel(val capacity : Int, val date : String)
