package pt.isel.ls.repo.jdbc.session

import org.junit.After
import org.junit.Test
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.createSessionDTO
import pt.isel.ls.repo.jdbc.JdbcSessionsRepo
import pt.isel.ls.utils.DATE_FORMATTER
import pt.isel.ls.utils.FIRST_GAME_ID
import pt.isel.ls.utils.FIRST_PLAYER_ID
import java.time.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.assertTrue

class RepoJdbcSessionTests {

    private val dataSource = PGSimpleDataSource().apply {
        setUrl(System.getenv("JDBC_DATABASE_URL"))
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
            date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER),
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
            date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER),
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
            date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER),
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
            date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID)
        )

        val s2 = createSessionDTO(
            capacity = 2,
            date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER),
            game = FIRST_GAME_ID,
            players = listOf(FIRST_PLAYER_ID + 1)
        )

        val sid = repo.createSession(s1)
        assert(sid > 0)

        val sid2 = repo.createSession(s2)
        assert(sid2 > 0)

        val sessions = repo.getListOfSessions(FIRST_GAME_ID, null, null, null, 0, 2)
        assertTrue(sessions.size == 2)
    }

}