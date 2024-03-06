package pt.isel.ls.services

import pt.isel.ls.domain.Session
import pt.isel.ls.domain.checkIfCanAdd
import pt.isel.ls.domain.toState
import pt.isel.ls.repo.Exceptions
import pt.isel.ls.repo.interfaces.GamesRepo
import pt.isel.ls.repo.interfaces.PlayersRepo
import pt.isel.ls.repo.interfaces.SessionRepo
import pt.isel.ls.utils.CAPACITY_LOWER_BOUND
import pt.isel.ls.utils.CAPACITY_UPPER_BOUND
import pt.isel.ls.utils.toDate
import java.util.*

class SessionServices(
    private val pRepo : PlayersRepo,
    private val gRepo : GamesRepo,
    private val sRepo : SessionRepo
) {

    fun createSession(token : String, gid : Int, capacity : Int, startDate: String) : Int {

        val pid = pRepo.getPlayerIdByToken(token)
        if(!gRepo.checkGameExists(gid))
            throw Exceptions.GameNotFound("Game $gid does not exist")

        check(capacity in CAPACITY_LOWER_BOUND..CAPACITY_UPPER_BOUND) {"Invalid capacity $capacity"}

        val date = startDate.toDate()

        return sRepo.createSession(pid, gid, capacity, date)
    }

    fun addPlayerToSession(token : String, sid : Int) {

        val pid = pRepo.getPlayerIdByToken(token)

        if(!sRepo.checkSessionExists(sid))
            throw Exceptions.SessionNotFound

        val session = sRepo.getSession(sid)
        session.checkIfCanAdd()

        return sRepo.addPlayerToSession(sid, pid)
    }

    fun getSession(sid : Int) : Session =
        sRepo.getSession(sid)

    fun getListOfSessions(gid : Int, startDate : String?, state : String?, pid : Int?, skip : Int, limit : Int) : List<Session> {
        if(!gRepo.checkGameExists(gid))
            throw Exceptions.GameNotFound("Game $gid does not exist")

        val sState = state?.toState()
        val date = startDate?.toDate()

        check(skip > 0) { "Skip value must be positive" }
        check(limit > 0) { "Limit value must be positive"}

        return sRepo.getListOfSessions(gid, date, sState, pid, skip, limit)

    }

}