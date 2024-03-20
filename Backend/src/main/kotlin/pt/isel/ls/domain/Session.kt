package pt.isel.ls.domain

import kotlinx.serialization.Serializable

fun String.isValidState() = this.uppercase().let { it == "OPEN" || it == "CLOSED" }
fun String.toState() = this.uppercase() == "CLOSED"

@Serializable
data class Session(val id: Int, val capacity: Int, val date: String, val game: Int, val closed : Boolean, val players: List<Int>)

fun Session.checkIfSessionClosed() = closed
fun Session.checkPlayerInSession(player : Int) = players.contains(player)
