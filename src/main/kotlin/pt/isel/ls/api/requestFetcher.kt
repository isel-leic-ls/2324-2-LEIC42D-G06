package pt.isel.ls.api

import org.http4k.core.Request
import org.http4k.routing.path
import pt.isel.ls.utils.LIMIT_DEFAULT
import pt.isel.ls.utils.SKIP_DEFAULT

fun Request.getSessionID() : Int {
    return path("sid")?.toInt() ?: throw IllegalArgumentException("Session ID not found")
}

fun Request.getSkipAndLimit() : Pair<Int, Int> {
    val skip = query("skip")?.toInt() ?: SKIP_DEFAULT
    val limit = query("top")?.toInt() ?: LIMIT_DEFAULT
    return skip to limit
}

fun Request.getAuthorizationToken() : String {
    val token = header("Authorization") ?: throw IllegalArgumentException("No Authorization header")
    if(!token.startsWith("Bearer ")) throw IllegalArgumentException("Invalid Authorization header")
    return token.substring(7)
}