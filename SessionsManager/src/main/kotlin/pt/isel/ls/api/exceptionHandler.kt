package pt.isel.ls.api

import kotlinx.serialization.Serializable
import org.http4k.core.Response
import org.http4k.core.Status


@Serializable
data class Problem(val description: String)

fun exceptionAwareScope(block: () -> Response): Response {
    return try {
        block()
    } catch (e: Exception) {
        exceptionHandler(e)
    }
}

fun exceptionHandler(e: Exception): Response {
    val status = errors[e::class] ?: Status.INTERNAL_SERVER_ERROR
    return Response(status).toJson(Problem(e.message ?: "Internal Server Error"))
}