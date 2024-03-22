package pt.isel.ls.repo.jdbc.players

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.repo.jdbc.JdbcPlayersRepo
import kotlin.test.*

class RepoJdbcPlayerTests {
    private val dataSource = PGSimpleDataSource().apply {
        setUrl(System.getenv("JDBC_DATABASE_URL"))
    }

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
        val repo = JdbcPlayersRepo(dataSource)
        val pId = repo.createPlayer("Trubin", "trubin1@gmail.com", "3ad7db4b-c5a9-42fe-9094-852f94c57cb9", "vasco123")
        assertEquals(pId, repo.getPlayer(pId).id)
    }

    @Test
    fun `creating a player`() {
        val repo = JdbcPlayersRepo(dataSource)
        val pId = repo.createPlayer("Trubin", "trubin1@gmail.com", "3ad7db4b-c5a9-42fe-9094-852f94c57cb9", "vasco123")
        assertTrue(pId > 0, "Player ID should be greater than zero")
    }

    @Test
    fun `check if a player exists by token`() {
        val repo = JdbcPlayersRepo(dataSource)
        val pId = repo.createPlayer("Trubin", "trubin1@gmail.com", "3ad7db4b-c5a9-42fe-9094-852f94c57cb9", "vasco123")
        val tPid = repo.getPlayerIdByToken("3ad7db4b-c5a9-42fe-9094-852f94c57cb9")
        assertEquals(pId,tPid, "Player ID should be the same")
    }

}