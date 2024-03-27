package pt.isel.ls.domain

import kotlinx.serialization.Serializable
import pt.isel.ls.utils.toDate
import java.time.LocalDateTime

fun String.isValidState() = this.uppercase().let { it == "OPEN" || it == "CLOSED" }
fun String.toState() = this.uppercase() == "CLOSED"

@Serializable
data class Session(val id: Int, val capacity: Int, val date: String, val game: Int, val closed : Boolean, val players: List<Int>)

fun Session.checkIfSessionFullCapacity() = closed

fun Session.checkIfSessionOngoing() = LocalDateTime.now().isAfter(date.toDate())
fun Session.checkIfCapacityCanBeUpdated(capacity : Int) =
    (capacity > this.capacity) ||
        (!closed && players.size <= capacity)

fun Session.checkIfPlayerIsOwner(player : Int) = players.first() == player
fun Session.checkPlayerInSession(player : Int) = players.contains(player)
