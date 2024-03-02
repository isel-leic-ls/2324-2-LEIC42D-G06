package pt.isel.ls.repo.session

import org.junit.Test
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.State
import pt.isel.ls.repo.MemSessionRepo
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MemSessionTests {

    @Test
    fun `creating a session test`() {

        //arrange
        val repo = MemSessionRepo()
        val pid = 1
        val gid = 1
        val startDate = Date.from(Instant.now())
        val capacity = 5

        //act
        val sid = repo.createSession(pid, gid, capacity, startDate)

        //assert
        assertTrue(sid > 0)

        //act
        val session = repo.getSession(sid)

        // assert
        assertTrue(session.capacity == capacity &&
            session.date == startDate && session.game == gid &&
                session.players.size == 1 && session.players.contains(pid) &&
                session.state == State.OPEN
        )
    }

    @Test
    fun `adding players to a session test`() {
        //arrange
        val repo = MemSessionRepo()
        val players = listOf(1,2)
        val gid = 1
        val capacity = 5
        val sid = repo.createSession(players[0], gid, capacity, Date.from(Instant.now()))
        val pid = players[1]

        //act
        repo.addPlayerToSession(sid, pid)

        val session = repo.getSession(sid)
        // assert
        assertTrue(session.players.size == 2 && session.players.contains(pid))
    }

    @Test
    fun `full session should yield closed state`() {
        //arrange
        val repo = MemSessionRepo()
        val players = listOf(1,2)
        val gid = 1
        val capacity = 2
        val sid = repo.createSession(players[0], gid, capacity, Date.from(Instant.now()))

        //act
        repo.addPlayerToSession(sid, players[1])
        val session = repo.getSession(sid)

        //assert
        assertEquals(session.state, State.CLOSED)
    }

    @Test
    fun `getting a list of games with the same game Id and no additional params`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val players = listOf(1,2)
        val capacity = 5
        val sid = repo.createSession(players[0], gid, capacity, Date.from(Instant.now()))
        val sid2 = repo.createSession(players[1], gid, capacity, Date.from(Instant.now()))

        //act
        val sessions = repo.getListOfSessions(gid, null, null, null)
        val session1 = repo.getSession(sid)
        val session2 = repo.getSession(sid2)

        //assert
        assertTrue(sessions.contains(session1) && sessions.contains(session2))
    }

    @Test
    fun `getting a list of games with a certain player id`() {
        //arrange
        val repo = MemSessionRepo()
        val games = listOf(1,2)
        val players = listOf(1,2)
        val capacity = 5
        val sessionCount = 5
        val sids = (0..sessionCount)
            .map { repo.createSession(players[it % 2], games[it % 2], capacity, Date.from(Instant.now()) ) }

        //act
        val sessions = repo.getListOfSessions(games[0], null, null, players[0])
        val session1 = repo.getSession(sids[0])
        val session2 = repo.getSession(sids[2])
        val session3 = repo.getSession(sids[4])

        //assert
        assertTrue(sessions.size == 3 &&
            sessions.containsAll(listOf(session1, session2, session3))
        )
    }

    // These 2 tests that fail with an exception are to be fixed with service logic
    @Test
    fun `getting a session that doesnt exist`() {
        //arrange
        val repo = MemSessionRepo()
        val sid = 10

        //act & assert
        assertFailsWith<NoSuchElementException> {
            repo.getSession(sid)
        }
    }

    @Test
    fun `adding a player to a session that doesnt exist`() {
        //arrange
        val repo = MemSessionRepo()
        val sid = 10
        val pid = 1

        //act & assert
        assertFailsWith<NoSuchElementException> {
            repo.addPlayerToSession(sid, pid)
        }
    }

    @Test
    fun `getting a list of games when list is empty`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1

        //act
        val sessions = repo.getListOfSessions(gid, null, null, null)

        //assert
        assertTrue(sessions.isEmpty())
    }

    @Test
    fun `getting a list of games with closed state`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val players = listOf(1,2)
        val capacity = 2
        val sid = repo.createSession(players[0], gid, capacity, Date.from(Instant.now()))
        val sid2 = repo.createSession(players[1], gid, capacity, Date.from(Instant.now()))
        repo.addPlayerToSession(sid, players[1])
        repo.addPlayerToSession(sid2, players[0])

        //act
        val sessions = repo.getListOfSessions(gid, null, State.CLOSED, null)
        val session1 = repo.getSession(sid)
        val session2 = repo.getSession(sid2)

        //assert
        assertTrue(sessions.size == 2 &&
            sessions.all { it.state == State.CLOSED} &&
            sessions.containsAll(listOf(session1, session2))
        )
    }

    @Test
    fun `creating sessions concurrently test`() {
        //arrange
        val repo = MemSessionRepo()
        val threadCount = 20
        val gid = 1
        val startDate = Date.from(Instant.now())
        val capacity = 5
        val sids = ConcurrentLinkedQueue<Int>()
        val sessions = ConcurrentLinkedQueue<Session>()

        //act
        val threads = (0 until threadCount).map {
            val t = Thread {
                val sid = repo.createSession(it + 1, gid, capacity, startDate)
                sids.add(sid)
                val session = repo.getSession(sid)
                sessions.add(session)
            }
            t.start()
            t
        }

        threads.forEach { it.join() }

        //assert
        assertTrue(sessions.size == threadCount &&
            sessions.all { it.capacity == capacity && it.date == startDate && it.game == gid && it.state == State.OPEN }
        )
        // check that every session id is in the range [1, threadCount] and that there are no duplicates
        assertTrue(sids.all { it in 1..threadCount } && sids.groupingBy { it }.eachCount().all { it.value == 1 })
    }
}