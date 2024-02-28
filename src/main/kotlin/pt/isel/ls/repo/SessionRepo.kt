package pt.isel.ls.repo

import pt.isel.ls.domain.Session
import java.util.*

interface SessionRepo {
    fun createSession(gid : Int, capacity : Int, startDate : Date) : Int
    fun addPlayerToSession(sid : Int) : Unit
    fun getSession(sid : Int) : Session
    fun getListOfSessions(gid : Int, date : Date, state : String, pid : Int) : List<Session> // State can be Sealed Class....
}