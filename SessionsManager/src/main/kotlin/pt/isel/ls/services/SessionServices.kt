package pt.isel.ls.services

import SessionRepo
import pt.isel.ls.domain.*
import pt.isel.ls.AppException
import pt.isel.ls.repo.interfaces.GamesRepo
import pt.isel.ls.repo.interfaces.PlayersRepo
import pt.isel.ls.utils.MIN_SESSION_CAPACITY
import pt.isel.ls.utils.checkIfDateIsAfterNow
import pt.isel.ls.utils.isDateWellFormatted

class SessionServices(
    private val pRepo : PlayersRepo,
    private val gRepo : GamesRepo,
    private val sRepo : SessionRepo
) {

    fun createSession(token : String, gid : Int, capacity : Int, startDate: String) : Int {
        val pid = pRepo.getPlayerIdByToken(token)

        checkGameExists(gid)
        checkDateFormat(startDate)

        return sRepo.createSession(createSessionDTO(capacity, startDate, gid, listOf(pid)))
    }

    fun addPlayerToSession(token : String, sid : Int) {
        val pid = pRepo.getPlayerIdByToken(token)

        checkSessionExists(sid)
        val session = sRepo.getSession(sid)
        checkSessionOngoing(session)
        checkSessionFullCapacity(session)
        checkPlayerInSession(session, pid)

        return sRepo.addPlayerToSession(sid, pid)
    }

    fun getSession(sid : Int) : Session =
        sRepo.getSession(sid)

    fun updateSession(token : String, sid : Int, date : String, capacity : Int) {
        val pid = pRepo.getPlayerIdByToken(token)

        require(capacity >= MIN_SESSION_CAPACITY) {
            "Capacity must be greater than $MIN_SESSION_CAPACITY"
        }
        require(date.isDateWellFormatted()) { "Invalid date format $date" }
        checkDateAfterNow(date)

        checkSessionExists(sid)
        val session = sRepo.getSession(sid)
        checkIfCapacityCanBeUpdated(session, capacity)
        checkSessionOngoing(session)

        checkIfPlayerIsNotOwner(session, pid)

        sRepo.updateSession(sid, date, capacity)
    }

    fun deleteSession(token : String, sid : Int) {
        val pid = pRepo.getPlayerIdByToken(token)
        checkSessionExists(sid)
        val session = sRepo.getSession(sid)
        checkIfPlayerIsNotOwner(session, pid)
        sRepo.deleteSession(sid)
    }

    fun deletePlayerFromSession(token : String, sid : Int) {
        val pid = pRepo.getPlayerIdByToken(token)

        checkSessionExists(sid)
        val session = sRepo.getSession(sid)
        checkPlayerNotInSession(session, pid)
        checkSessionOngoing(session)

        sRepo.deletePlayerFromSession(sid, pid)
    }

    fun getListOfSessions(
        gid: Int?, startDate: String?, state: String?, pid: Int?, skip: Int, limit: Int
    ): Pair<List<Session>, Int> {
        if (gid != null) checkGameExists(gid)
        if (startDate != null) checkDateFormat(startDate)
        if (state != null) checkState(state)

        val sState = state?.toState()

        require(skip >= 0) { "Skip value must be positive" }
        require(limit > 0) { "Limit value must be positive non-zero" }

        return sRepo.getListOfSessions(gid, startDate, sState, pid, skip, limit)
    }

    private fun checkSessionExists(sid : Int) {
        if(!sRepo.checkSessionExists(sid))
            throw AppException.SessionNotFound("Session $sid does not exist")
    }

    private fun checkGameExists(gid : Int) {
        if(!gRepo.checkGameExistsById(gid))
            throw AppException.GameNotFound("Game $gid does not exist")
    }

    private fun checkDateFormat(date : String) {
        if(!date.isDateWellFormatted())
            throw IllegalArgumentException("Invalid date format $date")
    }

    private fun checkState(state : String) {
        if(!state.isValidState())
            throw IllegalArgumentException("Invalid state $state")
    }

    private fun checkSessionFullCapacity(session: Session) {
        if(session.checkIfSessionFullCapacity())
            throw AppException.SessionClosed("Session ${session.id} is closed")
    }

    private fun checkSessionOngoing(session: Session) {
        if(session.checkIfSessionOngoing())
            throw AppException.SessionClosed("Session ${session.id} is closed")
    }

    private fun checkPlayerInSession(session: Session, pid: Int) {
        if(session.checkPlayerInSession(pid))
            throw AppException.PlayerAlreadyInSession("Player $pid is already in session ${session.id}")
    }

    private fun checkPlayerNotInSession(session: Session, pid: Int) {
        if(!session.checkPlayerInSession(pid))
            throw AppException.PlayerNotFoundInSession("Player $pid is not in session ${session.id}")
    }

    private fun checkDateAfterNow(date: String) {
        if(!date.checkIfDateIsAfterNow())
            throw IllegalArgumentException("Date must be after now")
    }

    private fun checkIfPlayerIsNotOwner(session: Session, pid: Int) {
        if(!session.checkIfPlayerIsOwner(pid))
            throw AppException.PlayerCantDeleteSession("Player $pid can't delete session ${session.id}")
    }

    private fun checkIfCapacityCanBeUpdated(session: Session, capacity: Int) {
        if(!session.checkIfCapacityCanBeUpdated(capacity))
            throw IllegalArgumentException("Capacity can't be updated")
    }
}