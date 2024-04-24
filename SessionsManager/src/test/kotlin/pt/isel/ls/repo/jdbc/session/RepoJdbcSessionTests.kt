package pt.isel.ls.repo.jdbc.session

import org.junit.After
import org.junit.Test
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.SessionDTO
import pt.isel.ls.domain.createSessionDTO
import pt.isel.ls.repo.jdbc.JdbcSessionsRepo
import pt.isel.ls.utils.*
import java.time.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.assertTrue

class RepoJdbcSessionTests {

    private val dataSource = PGSimpleDataSource().apply {
        setUrl(Environment.DATABASE_TEST_URL)
    }

    private val repo = JdbcSessionsRepo(dataSource)

    @BeforeTest
    fun setup() : Unit =
        dataSource.connection.use {
            it.prepareCall("Call removeTables()").execute()
            it.prepareCall("Call createTables()").execute()
            it.prepareCall("Call populateTables()").execute()
        }

    @After
    fun afterTestCleanup(): Unit =
        dataSource.connection.use {
            val stmt = it.prepareStatement("DELETE FROM SessionPlayer")
            val stmt2 = it.prepareStatement("DELETE FROM Session")
            stmt.executeUpdate()
            stmt2.executeUpdate()
        }


    @Test
    fun `create session and retrieve session`() {
        val s = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID)
        )

        val sid = repo.createSession(s)
        assert(sid > 0)

        val session = repo.getSession(sid)
        assertTrue(session.capacity == 2 && session.game == FIRST_GAME_ID && session.players.size == 1)
    }

    @Test
    fun `add player to session`() {
        val s = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID)
        )

        val sid = repo.createSession(s)
        assert(sid > 0)

        repo.addPlayerToSession(sid, FIRST_PLAYER_ID + 1)

        val session = repo.getSession(sid)
        assertTrue(session.capacity == 2 && session.game == FIRST_GAME_ID && session.players.size == 2)
    }

    @Test
    fun `check session exists`() {
        val s = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID)
        )

        val sid = repo.createSession(s)
        assert(sid > 0)

        assertTrue(repo.checkSessionExists(sid))
    }

    @Test
    fun `get list of sessions`() {
        val s1 = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID)
        )

        val s2 = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID + 1)
        )

        val sid = repo.createSession(s1)
        assert(sid > 0)

        val sid2 = repo.createSession(s2)
        assert(sid2 > 0)

        val result = repo.getListOfSessions(FIRST_GAME_ID, null, null, null, 0, 2)
        assertTrue(result.first.size == 2)
    }


    @Test
    fun `delete a session`() {
        val s = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID)
        )

        val sid = repo.createSession(s)
        assert(sid > 0)

        repo.deleteSession(sid)
        assertTrue(!repo.checkSessionExists(sid))
    }

    @Test
    fun `delete the only player from a session should remove the session`() {
        val s = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID)
        )

        val sid = repo.createSession(s)
        assert(sid > 0)

        repo.deletePlayerFromSession(sid, FIRST_PLAYER_ID)
        assertTrue(!repo.checkSessionExists(sid))
    }

    @Test
    fun `delete a player from a session`() {
        val s = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID, FIRST_PLAYER_ID + 1)
        )

        val sid = repo.createSession(s)
        assert(sid > 0)

        repo.addPlayerToSession(sid, FIRST_PLAYER_ID + 2)

        repo.deletePlayerFromSession(sid, FIRST_PLAYER_ID)
        val session = repo.getSession(sid)
        assertTrue(session.players.size == 1)
    }

    @Test
    fun `update session`() {
        val s = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID)
        )

        val sid = repo.createSession(s)
        assert(sid > 0)

        repo.updateSession(sid, LocalDateTime.now().plusDays(2).format(DATE_TIME_FORMATTER), 3)
        val session = repo.getSession(sid)
        assertTrue(session.capacity == 3 && session.date.toDate().isAfter(LocalDateTime.now().plusDays(1)))
    }

    @Test
    fun `update session capacity of almost full session`() {
        val s = createSessionDTO(
            capacity = 3,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID)
        )

        val sid = repo.createSession(s)
        assert(sid > 0)

        repo.addPlayerToSession(sid, FIRST_PLAYER_ID + 1)

        repo.updateSession(sid, LocalDateTime.now().plusDays(2).format(DATE_TIME_FORMATTER), 2)
        val session = repo.getSession(sid)
        assertTrue(session.capacity == 2 && session.date.toDate().isAfter(LocalDateTime.now().plusDays(1)) && session.closed)
    }

    @Test
    fun `update a full session to a bigger capacity`() {
        val s = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID, FIRST_PLAYER_ID + 1)
        )

        val sid = repo.createSession(s)
        assert(sid > 0)

        repo.addPlayerToSession(sid, FIRST_PLAYER_ID + 2)

        repo.updateSession(sid, LocalDateTime.now().plusDays(2).format(DATE_TIME_FORMATTER), 3)
        val session = repo.getSession(sid)
        assertTrue(session.capacity == 3 && session.date.toDate().isAfter(LocalDateTime.now().plusDays(1)) && !session.closed)
    }

    @Test
    fun `get games player will participate`() {
        val s1 = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID, FIRST_PLAYER_ID + 1)
        )

        val s2 = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID + 1,
            players = listOf(FIRST_PLAYER_ID, FIRST_PLAYER_ID + 2)
        )

        val sid = repo.createSession(s1)
        assert(sid > 0)

        val sid2 = repo.createSession(s2)
        assert(sid2 > 0)

        val result = repo.getListOfGamesThatPlayerWillParticipate(FIRST_PLAYER_ID, 0, 10)
        assertTrue(result.first.size == 2)
    }

    @Test
    fun `get games player will participate with skip and limit`() {
        val s1 = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID, FIRST_PLAYER_ID + 1)
        )

        val s2 = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER),
            game = FIRST_GAME_ID + 1,
            players = listOf(FIRST_PLAYER_ID, FIRST_PLAYER_ID + 2)
        )

        val sid1 = repo.createSession(s1)
        assert(sid1 > 0)

        val sid2 = repo.createSession(s2)
        assert(sid2 > 0)

        val result = repo.getListOfGamesThatPlayerWillParticipate(FIRST_PLAYER_ID, 1, 1)
        assertTrue(result.first.size == 1)
    }

    @Test
    fun `get empty list of games that player will participate`() {
        val result = repo.getListOfGamesThatPlayerWillParticipate(FIRST_PLAYER_ID, 0, 10)
        assertTrue(result.first.isEmpty())
    }
}