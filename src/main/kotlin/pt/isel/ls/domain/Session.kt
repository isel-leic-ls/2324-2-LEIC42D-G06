package pt.isel.ls.domain

import java.util.Date

enum class State {

    OPEN, CLOSED;
    fun String.toState() = if(this == "OPEN") OPEN else CLOSED
    override fun toString(): String = if(this == OPEN) "OPEN" else "CLOSED"
}
data class Session(val id: Int, val capacity: Int, val date: Date, val game: Int, val state : State, val players: List<Int>)




