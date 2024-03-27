package pt.isel.ls.api.model

import kotlinx.serialization.Serializable
import pt.isel.ls.domain.Session
@Serializable
data class SessionRetrievalOutputModel(val session : Session)