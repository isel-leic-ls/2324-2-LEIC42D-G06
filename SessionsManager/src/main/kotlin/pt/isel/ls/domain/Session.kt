package pt.isel.ls.domain

import kotlinx.serialization.Serializable
import pt.isel.ls.AppException
import pt.isel.ls.utils.checkIfDateIsAfterNow
import pt.isel.ls.utils.toDate
import java.time.LocalDateTime

fun String.isValidState() = this.uppercase().let { it == "OPEN" || it == "CLOSED" }
fun String.toState() = this.uppercase() == "CLOSED"

@Serializable
data class Session(val id: Int, val capacity: Int, val date: String, val game: Int, val closed : Boolean, val players: List<Int>)

fun Session.checkIfCapacityCanBeUpdated(capacity : Int) =
    (capacity > this.capacity) ||
        (!closed && players.size <= capacity)

fun Session.checkIfPlayerIsOwner(player : Int) = players.first() == player
fun Session.checkPlayerInSession(player : Int) = players.contains(player)

fun validatePlayerRemoval(session: Session, pid: Int) {
    if(!session.checkPlayerInSession(pid)) throw AppException.PlayerNotFoundInSession("Player $pid is not in session ${session.id}")
}

fun validateSessionUpdate(session : Session, date: String, capacity: Int, pid: Int) {
    if(!session.checkIfPlayerIsOwner(pid)) throw AppException.PlayerCantUpdateSession("Player $pid can't update session ${session.id}")
    if(!date.checkIfDateIsAfterNow()) throw IllegalArgumentException("Date must be after now")
    if(!session.checkIfCapacityCanBeUpdated(capacity)) throw IllegalArgumentException("Capacity can't be updated")
}

fun validatePlayerAddition(session : Session, pid : Int) {
    if(session.checkPlayerInSession(pid)) throw AppException.PlayerAlreadyInSession("Player $pid is already in session ${session.id}")
    if(session.closed) throw AppException.SessionClosed("Session ${session.id} is closed")
}


