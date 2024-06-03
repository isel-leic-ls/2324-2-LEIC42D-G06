package pt.isel.ls.api

import org.http4k.core.Request
import org.http4k.routing.path
import pt.isel.ls.AppException
import pt.isel.ls.api.model.GamesListInputModel
import pt.isel.ls.api.model.SessionListRetrievalInputModel
import pt.isel.ls.utils.LIMIT_DEFAULT
import pt.isel.ls.utils.SKIP_DEFAULT
import java.util.*


const val AUTHORIZATION_HEADER = "Authorization"
const val BEARER = "Bearer "
const val gameId = "gid"
const val gameName = "gname"
const val SESSION_ID = "sid"
const val SKIP = "skip"
const val LIMIT = "limit"
const val USERNAME = "name"

fun Request.getGameId(): Int {
    return path(gameId)?.toIntOrNull() ?: throw IllegalArgumentException("Invalid game ID")
}

fun Request.getGameName(): String {
    return path(gameName) ?: throw IllegalArgumentException("Invalid game name")
}

fun Request.getPartialGameName(): String {
    return query(gameName) ?: throw IllegalArgumentException("Invalid game name")
}

fun Request.getSessionID(): Int {
    return path(SESSION_ID)?.toIntOrNull() ?: throw IllegalArgumentException("Invalid session ID")
}

fun Request.getSkipAndLimit(): Pair<Int, Int> {
    val skip = query(SKIP)
        ?.let { it.toIntOrNull() ?: throw IllegalArgumentException("Invalid skip value") }
        ?: SKIP_DEFAULT

    val limit = query(LIMIT)
        ?.let { it.toIntOrNull() ?: throw IllegalArgumentException("Invalid limit value") }
        ?: LIMIT_DEFAULT

    return skip to limit
}

fun Request.getGamesListInputModel(): GamesListInputModel {
    val genres = query("genres")?.split(",") ?: emptyList()
    val developer = query("developer") ?: ""
    return GamesListInputModel(genres, developer)
}

fun Request.getSessionsListInputModel(): SessionListRetrievalInputModel {
    val gid = query("gid")?.toIntOrNull()
    val date = query("date")
    val state = query("state")
    val pid = query("pid")?.toIntOrNull()
    return SessionListRetrievalInputModel(gid, date, state, pid)
}



fun Request.getAuthorizationToken(): String {
    val token = header(AUTHORIZATION_HEADER) ?: throw AppException.InvalidAuthorization("No Authorization header")
    if (!token.startsWith(BEARER)) throw AppException.InvalidAuthorization("Invalid Authorization header")

    val tokenValue = token.substring(BEARER.length)
    try {
        UUID.fromString(tokenValue)
    } catch (e: Exception) {
        throw AppException.InvalidAuthorization("Invalid token format")
    }
    return tokenValue
}

fun Request.getPlayerDetails(): Int{
    return path("pid")?.toIntOrNull() ?: throw IllegalArgumentException("Invalid player ID")
}

fun Request.getUsername(): String {
    return query(USERNAME) ?: throw IllegalArgumentException("Invalid username")
}