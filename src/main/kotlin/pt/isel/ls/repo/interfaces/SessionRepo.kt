package pt.isel.ls.repo.interfaces

import pt.isel.ls.domain.Player
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.State
import java.util.*

interface SessionRepo {
    fun createSession(pid : Int, gid : Int, capacity : Int, startDate : Date) : Int
    fun addPlayerToSession(sid : Int, player : Int)
    fun getSession(sid : Int) : Session

    fun getListOfSessions(
        gid : Int,
        date : Date?,
        state : State?,
        pid : Int?,
        skip : Int,
        limit : Int) : List<Session>
}