package pt.isel.ls.domain

import pt.isel.ls.utils.MIN_SESSION_CAPACITY
import pt.isel.ls.utils.isDateTimeWellFormatted
import pt.isel.ls.utils.toDate
import java.time.LocalDateTime

data class SessionDTO(val capacity: Int, val date: String, val game: Int, val closed : Boolean = false, val players: List<Int>) {
    init {
        validateSessionCredentials(capacity, date)
        require(date.toDate().isAfter(LocalDateTime.now())) { "Invalid date $date" }
    }
}

fun validateSessionCredentials(capacity: Int, date: String) {
    require(date.isDateTimeWellFormatted()) { "Invalid date format $date" }
    require(capacity >= MIN_SESSION_CAPACITY) { "Invalid capacity $capacity" }
}

fun createSessionDTO(capacity: Int, date: String, game: Int, players: List<Int>) =
    SessionDTO(
        capacity = capacity,
        date = date,
        game = game,
        players = players
    )

fun SessionDTO.toSession(id : Int) =
    Session(id, capacity, date, game, closed, players)
