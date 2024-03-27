package pt.isel.ls.api.model

import kotlinx.serialization.Serializable

@Serializable
data class SessionListRetrievalInputModel(val gid : Int, val date : String?, val state : String?, val pid : Int?)