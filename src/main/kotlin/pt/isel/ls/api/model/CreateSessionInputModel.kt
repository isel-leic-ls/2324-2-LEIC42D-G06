package pt.isel.ls.api.model

import kotlinx.serialization.Serializable
@Serializable
data class CreateSessionInputModel(val gid : Int, val capacity : Int, val date : String)