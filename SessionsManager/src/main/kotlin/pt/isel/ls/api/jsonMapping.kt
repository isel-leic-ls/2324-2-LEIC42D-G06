package pt.isel.ls.api

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response

inline fun <reified T> Request.fromJson(): T {
    try {
        return Json.decodeFromString(bodyString())
    } catch (e: Exception) {
        throw IllegalArgumentException("Invalid JSON ${e.message}")
    }
}

inline fun <reified T : Any> Response.toJson(data: T): Response{
    return header("Content-Type", "application/json")
        .body(Json.encodeToString(data))
}