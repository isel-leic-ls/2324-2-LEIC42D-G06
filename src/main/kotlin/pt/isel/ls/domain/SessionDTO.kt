package pt.isel.ls.domain

import pt.isel.ls.repo.DomainException
import pt.isel.ls.utils.DATE_FORMATTER
import java.time.LocalDateTime

data class SessionDTO(val capacity: Int, val date: LocalDateTime, val game: Int, val closed : Boolean, val players: List<Int>) {
    init {
        require(capacity > 1) { "Invalid capacity $capacity" }
        if(date.isBefore(LocalDateTime.now()))
            throw DomainException.IllegalDate("Invalid date ${date.format(DATE_FORMATTER)}")
    }
}

fun createSessionDTO(capacity: Int, date: LocalDateTime, game: Int, players: List<Int>) =
    SessionDTO(capacity, date, game, false, players)

fun SessionDTO.toSession(id : Int) =
    Session(id, capacity, date, game, closed, players)
