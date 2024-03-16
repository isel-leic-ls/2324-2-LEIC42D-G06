package pt.isel.ls.domain

import kotlinx.serialization.Serializable
import pt.isel.ls.api.LocalDateTimeSerializer
import java.time.LocalDateTime

fun String.isValidState() = this.uppercase() == "OPEN" || this.uppercase() == "CLOSED"
fun String.toState() = this.uppercase() == "CLOSED"

@Serializable
data class Session(val id: Int, val capacity: Int, @Serializable(with = LocalDateTimeSerializer::class) val date: LocalDateTime, val game: Int, val closed : Boolean, val players: List<Int>) {
    fun addPlayer(player : Int) = run {
        copy(players = players + player, closed = players.size + 1 == capacity)
    }
}

fun Session.checkIfSessionClosed() = closed
fun Session.checkPlayerInSession(player : Int) = players.contains(player)
