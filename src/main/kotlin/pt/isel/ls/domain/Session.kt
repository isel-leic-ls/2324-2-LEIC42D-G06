package pt.isel.ls.domain

import java.time.LocalDateTime

fun String.isValidState() = this.uppercase() == "OPEN" || this.uppercase() == "CLOSED"
fun String.toState() = this.uppercase() == "CLOSED"
data class Session(val id: Int, val capacity: Int, val date: LocalDateTime, val game: Int, val closed : Boolean, val players: List<Int>) {
    fun addPlayer(player : Int) = run {
        copy(players = players + player, closed = players.size + 1 == capacity)
    }
}

fun Session.checkIfSessionClosed() = closed
fun Session.checkPlayerInSession(player : Int) = players.contains(player)
