package pt.isel.ls.api

import org.http4k.core.Request
import org.http4k.routing.path
import pt.isel.ls.AppException
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

fun Request.getGameId(): Int {
    return path(gameId)?.toIntOrNull() ?: throw IllegalArgumentException("Game ID not found")
}

fun Request.getGameName(): String {
    return path(gameName) ?: throw IllegalArgumentException("Game name not found")
}

fun Request.getSessionID(): Int {
    return path(SESSION_ID)?.toIntOrNull() ?: throw IllegalArgumentException("Session ID not found")
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

fun Request.getAuthorizationToken(): String {
    val token = header(AUTHORIZATION_HEADER) ?: throw IllegalArgumentException("No Authorization header")
    if (!token.startsWith(BEARER)) throw IllegalArgumentException("Invalid Authorization header")

    val tokenValue = token.substring(BEARER.length)
    try {
        UUID.fromString(tokenValue)
    } catch (e: Exception) {
        throw AppException.InvalidToken("Invalid token format")
    }
    return tokenValue
}

fun Request.getPlayerDetails(): Int {
    return path("pid")?.toIntOrNull() ?: throw IllegalArgumentException("Player ID not found")
}