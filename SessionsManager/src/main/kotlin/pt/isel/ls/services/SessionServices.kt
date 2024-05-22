package pt.isel.ls.services

import pt.isel.ls.repo.interfaces.SessionRepo
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
        checkGameExists(gid)
        return sRepo.createSession(createSessionDTO(capacity, startDate, gid, listOf(pid)))
    }

    fun addPlayerToSession(token : String, sid : Int) {
        val pid = pRepo.getPlayerIdByToken(token)
        val session = sRepo.getSession(sid)
        validatePlayerAddition(session, pid)
        return sRepo.addPlayerToSession(sid, pid)
    }

    fun getSession(sid : Int) : Session =
        sRepo.getSession(sid)

    fun updateSession(token : String, sid : Int, date : String, capacity : Int) {
        val pid = pRepo.getPlayerIdByToken(token)
        validateSessionCredentials(capacity, date)
        val session = sRepo.getSession(sid)
        validateSessionUpdate(session, date, capacity, pid)
        sRepo.updateSession(sid, date, capacity)
    }
    fun deleteSession(token : String, sid : Int) {
        val pid = pRepo.getPlayerIdByToken(token)
        val session = sRepo.getSession(sid)
        if(!session.checkIfPlayerIsOwner(pid))
            throw AppException.PlayerCantDeleteSession("Player $pid can't delete session ${session.id}")
        sRepo.deleteSession(sid)
    }

    fun deletePlayerFromSession(token : String, sid : Int) {
        val pid = pRepo.getPlayerIdByToken(token)
        val session = sRepo.getSession(sid)
        validatePlayerRemoval(session, pid)
        sRepo.deletePlayerFromSession(sid, pid)
    }


    fun getListOfSessions(
        gid: Int?, startDate: String?, state: String?, pid: Int?, skip: Int, limit: Int
    ): Pair<List<Session>, Int> {
        if (gid != null) checkGameExists(gid)
        if (startDate != null) checkDateFormat(startDate)
        if (state != null) checkState(state)
        require(skip >= 0) { "Skip value must be positive" }
        require(limit > 0) { "Limit value must be positive non-zero" }

        val sState = state?.toState()
        return sRepo.getListOfSessions(gid, startDate, sState, pid, skip, limit)
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


}