import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionDTO
import java.time.LocalDateTime

interface SessionRepo {
    fun createSession(sessionDTO: SessionDTO) : Int
    fun addPlayerToSession(updatedSession : Session)
    fun getSession(sid : Int) : Session

    fun getListOfSessions(
        gid : Int,
        date : LocalDateTime?,
        state : Boolean?,
        pid : Int?,
        skip : Int,
        limit : Int
    ) : List<Session>

    fun checkSessionExists(sid : Int) : Boolean
}