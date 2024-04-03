package pt.isel.ls.api.model

import kotlinx.serialization.Serializable
import pt.isel.ls.domain.Session
@Serializable
data class SessionListRetrievalOutputModel(val sessions : List<Session>, val total: Int)