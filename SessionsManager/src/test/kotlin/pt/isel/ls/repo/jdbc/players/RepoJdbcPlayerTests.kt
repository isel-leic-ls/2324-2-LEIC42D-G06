package pt.isel.ls.repo.jdbc.players

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.AppException
import pt.isel.ls.repo.jdbc.JdbcPlayersRepo
import pt.isel.ls.utils.Environment
import kotlin.test.*


class RepoJdbcPlayerTests {
    private val dataSource = PGSimpleDataSource().apply {
        setUrl(Environment.DATABASE_TEST_URL)
    }

    private val repo = JdbcPlayersRepo(dataSource)

    @BeforeTest
    fun setup(): Unit =
        dataSource.connection.use {
            val stmt0 = it.prepareStatement("DELETE FROM SessionPlayer")
            stmt0.executeUpdate()
            val stmt1 = it.prepareStatement("DELETE FROM Session")
            stmt1.executeUpdate()
            val stmt2 = it.prepareStatement("DELETE FROM Game")
            stmt2.executeUpdate()
        }

    @AfterTest
    fun afterTestCleanup(): Unit =
        dataSource.connection.use {
            val stmt = it.prepareStatement("DELETE FROM Player")
            stmt.executeUpdate()
        }

    @Test
    fun `check if player exists by id`() {
        val pId = repo.createPlayer(
            "Trubin",
            "trubin1@gmail.com",
            "3ad7db4b-c5a9-42fe-9094-852f94c57cb9",
            "vasco123"
        )
        assertEquals(pId, repo.getPlayer(pId).id)
    }

    @Test
    fun `creating an invalid player`() {
        assertFailsWith<AppException.PlayerNotFound> {
            repo.getPlayer(1)
        }
    }

    @Test
    fun `creating a player`() {
        val pId = repo.createPlayer(
            "Trubin",
            "trubin1@gmail.com",
            "3ad7db4b-c5a9-42fe-9094-852f94c57cb9",
            "vasco123"
        )
        assertTrue(pId > 0, "Player ID should be greater than zero")
    }

    @Test
    fun `check if a player exists by token`() {
        val pId = repo.createPlayer(
            "Trubin",
            "trubin1@gmail.com",
            "3ad7db4b-c5a9-42fe-9094-852f94c57cb9",
            "vasco123"
        )
        val tPid = repo.getPlayerIdByToken("3ad7db4b-c5a9-42fe-9094-852f94c57cb9")
        assertEquals(pId, tPid, "Player ID should be the same")
    }

    @Test
    fun `get non existent player by token`() {
        assertFailsWith<AppException.PlayerNotFound> {
            repo.getPlayerIdByToken("3ad7db4b")
        }
    }
}