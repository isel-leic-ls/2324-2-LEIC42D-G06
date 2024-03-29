import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionDTO
import java.time.LocalDateTime

interface SessionRepo {
    fun createSession(dto: SessionDTO) : Int
    fun addPlayerToSession(sid: Int, pid: Int)
    fun getSession(sid : Int) : Session

    fun updateSession(sid : Int, date : String, capacity : Int)

    fun deleteSession(sid : Int)

    fun deletePlayerFromSession(sid : Int, pid : Int)

    fun getListOfSessions(
        gid : Int?,
        date : String?,
        state : Boolean?,
        pid : Int?,
        skip : Int,
        limit : Int
    ) : List<Session>

    fun checkSessionExists(sid : Int) : Boolean
}