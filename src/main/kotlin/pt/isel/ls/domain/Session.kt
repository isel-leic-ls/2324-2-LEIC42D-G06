package pt.isel.ls.domain

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

enum class State {

    OPEN, CLOSED;
    override fun toString(): String = if(this == OPEN) "OPEN" else "CLOSED"
}

fun String.toState() = when(this.uppercase()) {
    "OPEN" -> State.OPEN
    "CLOSED" -> State.CLOSED
    else -> error("State $this is not valid")
}
data class Session(val id: Int, val capacity: Int, val date: LocalDateTime, val game: Int, val state : State, val players: List<Int>)

fun Session.checkIfCanAdd() = state != State.CLOSED &&
        !Duration.between(date, LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())).isNegative




