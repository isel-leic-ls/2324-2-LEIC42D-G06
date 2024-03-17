package pt.isel.ls.api

import org.http4k.core.Request
import org.http4k.routing.path
import pt.isel.ls.utils.LIMIT_DEFAULT
import pt.isel.ls.utils.SKIP_DEFAULT


const val AUTHORIZATION_HEADER = "Authorization"
const val BEARER = "Bearer "
const val gameId = "gid"
const val session_id = "sid"
const val skip = "skip"
const val limit = "limit"

fun Request.getGameId(): Int {
    return path(gameId)?.toInt() ?: throw IllegalArgumentException("Game ID not found")
}

fun Request.getGameName(): String {
    return path(gameId) ?: throw IllegalArgumentException("Game name not found")
}

fun Request.getSessionID() : Int {
    return path(session_id)?.toInt() ?: throw IllegalArgumentException("Session ID not found")
}

fun Request.getSkipAndLimit() : Pair<Int, Int> {
    val skip = query(skip)
        ?.let{ it.toIntOrNull() ?: throw IllegalArgumentException("Invalid skip value") }
        ?: SKIP_DEFAULT

    val limit = query(limit)
        ?.let{ it.toIntOrNull() ?: throw IllegalArgumentException("Invalid limit value") }
        ?: LIMIT_DEFAULT
    return skip to limit
}

fun Request.getAuthorizationToken(): String {
    val token = header(AUTHORIZATION_HEADER) ?: throw IllegalArgumentException("No Authorization header")
    if (!token.startsWith(BEARER)) throw IllegalArgumentException("Invalid Authorization header")
    return token.substring(BEARER.length)
}