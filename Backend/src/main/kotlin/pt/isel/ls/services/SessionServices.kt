package pt.isel.ls.services

import SessionRepo
import pt.isel.ls.domain.*
import pt.isel.ls.AppException
import pt.isel.ls.repo.interfaces.GamesRepo
import pt.isel.ls.repo.interfaces.PlayersRepo
import pt.isel.ls.utils.isDateWellFormatted

class SessionServices(
    private val pRepo : PlayersRepo,
    private val gRepo : GamesRepo,
    private val sRepo : SessionRepo
) {

    fun createSession(token : String, gid : Int, capacity : Int, startDate: String) : Int {
        val pid = pRepo.getPlayerIdByToken(token)
            ?: throw AppException.InvalidAuthorization("Invalid token $token")

        checkGameExists(gid)
        checkDateFormat(startDate)

        return sRepo.createSession(createSessionDTO(capacity, startDate, gid, listOf(pid)))
    }

    fun addPlayerToSession(token : String, sid : Int) {
        val pid = pRepo.getPlayerIdByToken(token)
            ?: throw AppException.InvalidAuthorization("Invalid token $token")

        checkSessionExists(sid)
        val session = sRepo.getSession(sid)

        checkSessionClosed(session)
        checkPlayerInSession(session, pid)

        return sRepo.addPlayerToSession(sid, pid)
    }

    fun getSession(sid : Int) : Session =
        sRepo.getSession(sid)

    fun getListOfSessions(gid : Int, startDate : String?, state : String?, pid : Int?, skip : Int, limit : Int) : List<Session> {
        checkGameExists(gid)

        if(startDate != null) checkDateFormat(startDate)
        if(state != null) checkState(state)

        val sState = state?.toState()

        require(skip >= 0) { "Skip value must be positive" }
        require(limit > 0) { "Limit value must be positive non-zero"}

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

    private fun checkSessionClosed(session: Session) {
        if(session.checkIfSessionClosed())
            throw AppException.SessionClosed("Session ${session.id} is closed")
    }

    private fun checkPlayerInSession(session: Session, pid: Int) {
        if(session.checkPlayerInSession(pid))
            throw AppException.PlayerAlreadyInSession("Player $pid is already in session ${session.id}")
    }

}
