package pt.isel.ls.api.model

import kotlinx.serialization.Serializable
import pt.isel.ls.domain.Session1

@Serializable
data class SessionsListRetrievalOutputModelWithoutTotal(val sessions : List<Session1>)