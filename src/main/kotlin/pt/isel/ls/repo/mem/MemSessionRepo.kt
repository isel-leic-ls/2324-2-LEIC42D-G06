package pt.isel.ls.repo.mem

import pt.isel.ls.domain.Session
import pt.isel.ls.domain.State
import pt.isel.ls.repo.Exceptions
import pt.isel.ls.repo.interfaces.SessionRepo
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger


class MemSessionRepo : SessionRepo {
    private val sessions = ConcurrentLinkedQueue<Session>()
    private val currentId = AtomicInteger(1)

    override fun createSession(pid: Int, gid: Int, capacity: Int, startDate: LocalDateTime): Int {
        val id = currentId.getAndIncrement()
        val session = Session(id, capacity, startDate, gid, State.OPEN, listOf(pid))
        sessions.add(session)
        return id
    }

    override fun addPlayerToSession(sid: Int, player: Int) {
        sessions.find { it.id == sid }?.let { session ->
            val nPlayers = session.players + player
            val state = if (nPlayers.size == session.capacity) State.CLOSED else session.state
            val nSession = session.copy(players = nPlayers, state = state)
            sessions.remove(session)
            sessions.add(nSession)
        } ?: throw Exceptions.SessionNotFound
    }

    override fun getSession(sid: Int): Session =
        sessions.find { it.id == sid } ?: throw Exceptions.SessionNotFound

    override fun getListOfSessions(
        gid: Int,
        date: LocalDateTime?,
        state: State?,
        pid: Int?,
        skip: Int,
        limit: Int
    ): List<Session> =
        sessions.filter {
            it.game == gid &&
            date?.let { d -> it.date == d } ?: true &&
            state?.let { s -> it.state == s } ?: true &&
            pid?.let { p -> it.players.contains(p) } ?: true
        }.drop(skip).take(limit)

    override fun checkSessionExists(sid: Int): Boolean =
        sessions.any { it.id == sid }

}