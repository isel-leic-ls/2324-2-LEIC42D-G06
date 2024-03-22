package pt.isel.ls.repo.mem

import SessionRepo
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionDTO
import pt.isel.ls.domain.toSession
import pt.isel.ls.AppException
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class MemSessionRepo : SessionRepo {
    private val sessions = LinkedList<Session>()
    private val currentId = AtomicInteger(1)
    private val monitor = ReentrantLock()
    override fun createSession(dto: SessionDTO): Int {
        monitor.withLock {
            val id = currentId.getAndIncrement()
            val session = dto.toSession(id)
            sessions.add(session)
            return id
        }
    }

    override fun addPlayerToSession(sid : Int, pid : Int) {
        monitor.withLock {
            sessions.find { it.id == sid }?.let { s ->
                if(s.closed) throw AppException.SessionClosed("Session is closed")
                val nSession = s.copy(players = s.players + pid, closed = s.players.size + 1 == s.capacity)
                sessions.remove(s)
                sessions.add(nSession)
            } ?: throw AppException.SessionNotFound("Session not found with id $sid")
        }
    }

    override fun getSession(sid: Int): Session =
        monitor.withLock {
            sessions.find { it.id == sid }
            ?: throw AppException.SessionNotFound("Session not found with id $sid")
        }

    override fun getListOfSessions(
        gid: Int,
        date: String?,
        state: Boolean?,
        pid: Int?,
        skip: Int,
        limit: Int
    ): List<Session> =
        monitor.withLock {
            sessions.filter {
                it.game == gid &&
                date?.let { d -> it.date == d } ?: true &&
                state?.let { s -> it.closed == s } ?: true &&
                pid?.let { p -> it.players.contains(p) } ?: true
            }.drop(skip).take(limit)
        }

    override fun checkSessionExists(sid: Int): Boolean =
        sessions.any { it.id == sid }

}