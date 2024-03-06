package pt.isel.ls.repo.jdbc.games

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.repo.jdbc.JdbcGamesRepo
import kotlin.test.*


class JdbcGamesTests {
    private val dataSource = PGSimpleDataSource().apply {
        setUrl(System.getenv("JDBC_DATABASE_URL"))
    }

    //clears the SessionPlayer, Session and Game tables before each test
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

    //clears the Game table after each test
    @AfterTest
    fun afterTestCleanup(): Unit =
        dataSource.connection.use {
            val stmt = it.prepareStatement("DELETE FROM Game")
            stmt.executeUpdate()
        }

    //TODO add tests for checkGameExistsById

    //TODO add tests for checkGameExistsByName

    @Test
    fun `test game insertion given a name, a developer and a list of genres`() {
        val repo = JdbcGamesRepo(dataSource.connection)
        /*val returnedGameId = */repo.insert(
            "Game Name - Insertion Test Jdbc", "Generic Game Developer", listOf("genre1", "genre2")
        )
        //println("\nGame ID returned: $returnedGameId\n")
        //TODO should we add assertions here?
    }

    @Test
    fun `test getting a game by its id`() {
        val repo = JdbcGamesRepo(dataSource.connection)
        val name = "Generic Game Name"
        val dev = "Generic Game Developer"
        val genres = listOf("genre1", "genre2")

        val gid = repo.insert(name, dev, genres)
        val game = repo.getGameById(gid)

        assertEquals(gid, game.id)
        assertEquals(name, game.name)
        assertEquals(dev, game.dev)
        assertTrue { game.genres.contains("genre1") }
        assertTrue { game.genres.contains("genre2") }
    }

    //TODO add tests for getGameByName

    //TODO add tests for getListOfGames

    //TODO add tests for paging (skip and limit)
}