package pt.isel.ls.repo.jdbc.games

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

}