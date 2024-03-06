package pt.isel.ls.repo.interfaces

import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.State
import java.time.LocalDateTime
import java.util.*

interface SessionRepo {
    fun createSession(pid : Int, gid : Int, capacity : Int, startDate : LocalDateTime) : Int
    fun addPlayerToSession(sid : Int, player : Int)
    fun getSession(sid : Int) : Session

    fun getListOfSessions(
        gid : Int,
        date : LocalDateTime?,
        state : State?,
        pid : Int?,
        skip : Int,
        limit : Int
    ) : List<Session>

    fun checkSessionExists(sid : Int) : Boolean
}