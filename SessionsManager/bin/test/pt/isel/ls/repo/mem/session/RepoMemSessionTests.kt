package pt.isel.ls.repo.mem.session

import org.junit.Test
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.createSessionDTO
import pt.isel.ls.AppException
import pt.isel.ls.repo.mem.MemSessionRepo
import pt.isel.ls.utils.DATE_FORMATTER
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class RepoMemSessionTests {
    @Test
    fun `creating a session test`() {

        //arrange
        val repo = MemSessionRepo()
        val pid = 1
        val gid = 1
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val capacity = 5

        //act
        val sessionDTO = createSessionDTO(capacity, date, gid, listOf(pid))
        val sid = repo.createSession(sessionDTO)

        //assert
        assertTrue(sid > 0)

        //act
        val session = repo.getSession(sid)

        // assert
        assertTrue(
            session.capacity == capacity &&
                    session.date == date && session.game == gid &&
                    session.players.size == 1 && session.players.contains(pid) && !session.closed
        )
    }

    @Test
    fun `adding players to a session test`() {
        //arrange
        val repo = MemSessionRepo()
        val players = listOf(1, 2)
        val gid = 1
        val capacity = 5
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sessionDTO = createSessionDTO(capacity, date, gid, listOf(players[0]))
        val sid = repo.createSession(sessionDTO)
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
        val players = listOf(1, 2)
        val gid = 1
        val capacity = 2
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sessionDTO = createSessionDTO(capacity, date, gid, listOf(players[0]))
        val sid = repo.createSession(sessionDTO)

        //act
        repo.addPlayerToSession(sid, players[1])
        val session = repo.getSession(sid)

        //assert
        assertTrue(session.closed)
    }

    @Test
    fun `getting a list of games with the same game Id and no additional params`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val players = listOf(1, 2)
        val capacity = 5
        val skip = 0
        val limit = Int.MAX_VALUE
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)

        val sid = repo.createSession(createSessionDTO(capacity, date, gid, listOf(players[0])))
        val sid2 = repo.createSession(createSessionDTO(capacity, date, gid, listOf(players[1])))

        //act
        val sessions = repo.getListOfSessions(gid, null, null, null, skip, limit)
        val session1 = repo.getSession(sid)
        val session2 = repo.getSession(sid2)

        //assert
        assertTrue(sessions.contains(session1) && sessions.contains(session2))
    }

    @Test
    fun `getting a list of games with a certain player id`() {
        //arrange
        val repo = MemSessionRepo()
        val games = listOf(1, 2)
        val players = listOf(1, 2)
        val capacity = 5
        val sessionCount = 5
        val skip = 0
        val limit = Int.MAX_VALUE
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sids = (0..sessionCount)
            .map { repo.createSession(createSessionDTO(capacity, date, games[it % 2], listOf(players[it % 2]))) }

        //act
        val sessions = repo.getListOfSessions(games[0], null, null, players[0], skip, limit)
        val session1 = repo.getSession(sids[0])
        val session2 = repo.getSession(sids[2])
        val session3 = repo.getSession(sids[4])

        //assert
        assertTrue(
            sessions.size == 3 &&
                    sessions.containsAll(listOf(session1, session2, session3))
        )
    }

    // These 2 tests that fail with an exception are to be fixed with service logic
    @Test
    fun `getting a session that does not exist`() {
        //arrange
        val repo = MemSessionRepo()
        val sid = 10

        //act & assert
        assertFailsWith<AppException.SessionNotFound> {
            repo.getSession(sid)
        }
    }

    @Test
    fun `adding a player to a session that does not exist`() {
        //arrange
        val repo = MemSessionRepo()
        val sid = 10
        val pid = 1
        //act & assert
        assertFailsWith<AppException.SessionNotFound> {
            repo.addPlayerToSession(sid, pid)
        }
    }

    @Test
    fun `getting a list of games when list is empty`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val skip = 0
        val limit = Int.MAX_VALUE

        //act
        val sessions = repo.getListOfSessions(gid, null, null, null, skip, limit)

        //assert
        assertTrue(sessions.isEmpty())
    }

    @Test
    fun `getting a list of games skipping 2 games`() {
        //arrange
        val repo = MemSessionRepo()
        val skip = 2
        val limit = Int.MAX_VALUE
        val total = 5
        val gid = 1
        val capacity = 5
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)

        val sids = (0..total).map { repo.createSession(createSessionDTO(capacity, date, gid, listOf(it + 1))) }

        //act
        val sessions = repo.getListOfSessions(gid, null, null, null, skip, limit)

        //assert
        assertTrue(sessions.size == (total + 1 - skip) && sessions.all { it.game == gid && sids.contains(it.id) })

    }

    @Test
    fun `limiting the retrieval of games to 2 from a bigger list`() {
        //arrange
        val repo = MemSessionRepo()
        val skip = 0
        val limit = 2
        val total = 5
        val gid = 1
        val capacity = 5
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sids = (0..total).map { repo.createSession(createSessionDTO(capacity, date, gid, listOf(it + 1))) }

        //act
        val sessions = repo.getListOfSessions(gid, null, null, null, skip, limit)

        //assert
        assertTrue(sessions.size == limit && sessions.all { it.id in sids.take(limit) })

    }

    @Test
    fun `skipping all games should yield an empty list`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val capacity = 5
        val skip = Int.MAX_VALUE
        val limit = Int.MAX_VALUE
        val pid = 1
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        repo.createSession(createSessionDTO(capacity, date, gid, listOf(pid)))

        //act
        val sessions = repo.getListOfSessions(gid, null, null, null, skip, limit)

        //assert
        assertTrue(sessions.isEmpty())
    }

    @Test
    fun `limiting the search of games to 0 yields an empty list`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val capacity = 5
        val skip = 0
        val limit = 0
        val pid = 1
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        repo.createSession(createSessionDTO(capacity, date, gid, listOf(pid)))

        //act
        val sessions = repo.getListOfSessions(gid, null, null, null, skip, limit)

        //assert
        assertTrue(sessions.isEmpty())
    }

    @Test
    fun `skipping 2 games and taking 2 games from a list of 6 games should yield the sessions in the middle of the list`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val capacity = 5
        val total = 5
        val skip = 2
        val limit = skip
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)

        val sids = (0..total).map { repo.createSession(createSessionDTO(capacity, date, gid, listOf(it + 1))) }

        //act
        val sessions = repo.getListOfSessions(gid, null, null, null, skip, limit)

        //assert
        assertTrue(sessions.size == limit && sessions.all { it.id in sids.drop(skip).take(limit) })
    }

    @Test
    fun `getting a list of games with closed state`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val players = listOf(1, 2)
        val skip = 0
        val limit = Int.MAX_VALUE
        val capacity = 2
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sid = repo.createSession(createSessionDTO(capacity, date, gid, listOf(players[0])))
        val sid2 = repo.createSession(createSessionDTO(capacity, date, gid, listOf(players[1])))

        repo.addPlayerToSession(sid, players[1])
        repo.addPlayerToSession(sid2, players[0])

        //act
        val sessions = repo.getListOfSessions(gid, null, true , null, skip, limit)
        val sessionRetrieval = repo.getSession(sid)
        val sessionRetrieval2 = repo.getSession(sid2)

        //assert
        assertTrue(sessions.size == 2 &&
                sessions.all { it.closed } &&
                sessions.containsAll(listOf(sessionRetrieval, sessionRetrieval2))
        )
    }

    @Test
    fun `creating sessions concurrently test`() {
        //arrange
        val repo = MemSessionRepo()
        val threadCount = 20
        val gid = 1
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val capacity = 5
        val sids = ConcurrentLinkedQueue<Int>()
        val sessions = ConcurrentLinkedQueue<Session>()

        //act
        val threads = (0 until threadCount).map {
            val t = Thread {
                val sid = repo.createSession(createSessionDTO(capacity, startDate, gid, listOf(it + 1)))
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
                sessions.all { it.capacity == capacity && it.date == startDate && it.game == gid && !it.closed }
        )
        // check that every session id is in the range [1, threadCount] and that there are no duplicates
        assertTrue(sids.all { it in 1..threadCount } && sids.groupingBy { it }.eachCount().all { it.value == 1 })
    }

    @Test
    fun `removing a session`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val players = listOf(1, 2)
        val capacity = 5
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sid = repo.createSession(createSessionDTO(capacity, date, gid, listOf(players[0])))

        // act
        repo.deleteSession(sid)

        // assert
        assertFailsWith<AppException.SessionNotFound> {
            repo.getSession(sid)
        }
    }

    @Test
    fun `removing a player from a session`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val players = listOf(1, 2)
        val capacity = 5
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sid = repo.createSession(createSessionDTO(capacity, date, gid, listOf(players[0])))

        repo.addPlayerToSession(sid, players[1])
        // act
        repo.deletePlayerFromSession(sid, players[1])

        // assert
        val session = repo.getSession(sid)
        assertTrue(session.players.size == 1 && session.players.contains(players[0]))
    }

    @Test
    fun `removing the only player from a session`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val players = listOf(1)
        val capacity = 5
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sid = repo.createSession(createSessionDTO(capacity, date, gid, listOf(players[0])))

        // act
        repo.deletePlayerFromSession(sid, players[0])

        // assert
        assertFailsWith<AppException.SessionNotFound> {
            repo.getSession(sid)
        }
    }

    @Test
    fun `removing a player from a full session should yield an open session`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val players = listOf(1, 2)
        val capacity = 2
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sid = repo.createSession(createSessionDTO(capacity, date, gid, listOf(players[0])))

        repo.addPlayerToSession(sid, players[1])

        // act
        repo.deletePlayerFromSession(sid, players[1])

        // assert
        val session = repo.getSession(sid)
        assertTrue(session.players.size == 1 && !session.closed)
    }

    @Test
    fun `update a session`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val players = listOf(1, 2)
        val capacity = 5
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sid = repo.createSession(createSessionDTO(capacity, date, gid, listOf(players[0])))

        // act
        val newDate = LocalDateTime.now().plusDays(2).format(DATE_FORMATTER)
        val newCapacity = 10
        repo.updateSession(sid, newDate, newCapacity)

        // assert
        val session = repo.getSession(sid)
        assertTrue(session.date == newDate && session.capacity == newCapacity)
    }

    @Test
    fun `update a session that is almost full to a full capacity`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val players = listOf(1, 2)
        val capacity = 3
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sid = repo.createSession(createSessionDTO(capacity, date, gid, listOf(players[0])))

        repo.addPlayerToSession(sid, players[1])

        // act
        val newDate = LocalDateTime.now().plusDays(2).format(DATE_FORMATTER)
        val newCapacity = 2
        repo.updateSession(sid, newDate, newCapacity)

        // assert
        val session = repo.getSession(sid)
        assertTrue(session.date == newDate && session.capacity == newCapacity && session.closed)
    }

    @Test
    fun `update a session that is full to a bigger capacity`() {
        //arrange
        val repo = MemSessionRepo()
        val gid = 1
        val players = listOf(1, 2)
        val capacity = 2
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sid = repo.createSession(createSessionDTO(capacity, date, gid, listOf(players[0])))

        repo.addPlayerToSession(sid, players[1])

        // act
        val newDate = LocalDateTime.now().plusDays(2).format(DATE_FORMATTER)
        val newCapacity = 3
        repo.updateSession(sid, newDate, newCapacity)

        // assert
        val session = repo.getSession(sid)
        assertTrue(session.date == newDate && session.capacity == newCapacity && !session.closed)
    }
}