package pt.isel.ls.repo.mem

import SessionRepo
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionDTO
import pt.isel.ls.domain.toSession
import pt.isel.ls.repo.DomainException
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger


class MemSessionRepo : SessionRepo {
    private val sessions = ConcurrentLinkedQueue<Session>()
    private val currentId = AtomicInteger(1)

    override fun createSession(sessionDTO: SessionDTO): Int {
        val id = currentId.getAndIncrement()
        val session = sessionDTO.toSession(id)
        sessions.add(session)
        return id
    }

    override fun addPlayerToSession(updatedSession : Session) {
        sessions.find { it.id == updatedSession.id }?.let { session ->
            sessions.remove(session)
            sessions.add(updatedSession)
        } ?: throw DomainException.SessionNotFound("Session not found with id ${updatedSession.id}")
    }

    override fun getSession(sid: Int): Session =
        sessions.find { it.id == sid } ?: throw DomainException.SessionNotFound("Session not found with id $sid")

    override fun getListOfSessions(
        gid: Int,
        date: LocalDateTime?,
        state: Boolean?,
        pid: Int?,
        skip: Int,
        limit: Int
    ): List<Session> =
        sessions.filter {
            it.game == gid &&
            date?.let { d -> it.date == d } ?: true &&
            state?.let { s -> it.closed == s } ?: true &&
            pid?.let { p -> it.players.contains(p) } ?: true
        }.drop(skip).take(limit)

    override fun checkSessionExists(sid: Int): Boolean =
        sessions.any { it.id == sid }

}