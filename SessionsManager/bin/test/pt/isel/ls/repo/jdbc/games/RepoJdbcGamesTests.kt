package pt.isel.ls.repo.jdbc.games

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.repo.jdbc.JdbcGamesRepo
import pt.isel.ls.utils.FIRST_GAME_ID
import kotlin.test.*


class RepoJdbcGamesTests {
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

    @Test
    fun `check game exists by id`() {
        val repo = JdbcGamesRepo(dataSource)
        val gId = repo.insert("name", "developer", listOf("genre"))

        assertTrue { repo.checkGameExistsById(gId) }
        assertFalse { repo.checkGameExistsById(gId - 1) }
    }

    @Test
    fun `check game exists by name`() {
        val repo = JdbcGamesRepo(dataSource)
        val gName = "name"
        repo.insert(gName, "developer", listOf("genre"))

        assertTrue { repo.checkGameExistsByName(gName) }
        assertTrue { repo.checkGameExistsByName(gName.uppercase()) } //test case-insensitive
        assertFalse { repo.checkGameExistsByName("anotherName") }
    }

    @Test
    fun `game insertion given a name, a developer and a list of genres twice`() {
        val repo = JdbcGamesRepo(dataSource)
        val gId1 = repo.insert(
            "gName1 - Insertion Test Jdbc", "Developer XPTO", listOf("genre1", "genre2")
        )

        assertTrue(gId1 >= FIRST_GAME_ID)

        val gId2 = repo.insert(
            "gName2 - Insertion Test Jdbc", "Developer ABC", listOf("genreA")
        )

        assertTrue(gId2 >= FIRST_GAME_ID + 1)
    }

    @Test
    fun `get a game by id`() {
        val repo = JdbcGamesRepo(dataSource)
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

    @Test
    fun `get game by name`() {
        val repo = JdbcGamesRepo(dataSource)
        val name = "Generic Game Name"
        val dev = "Generic Game Developer"
        val genres = listOf("genre1", "genre2")

        val gid = repo.insert(name, dev, genres)
        val game = repo.getGameByName(name)

        assertEquals(gid, game.id)
        assertEquals(name, game.name)
        assertEquals(dev, game.dev)
        assertTrue { game.genres.contains("genre1") }
        assertTrue { game.genres.contains("genre2") }
    }

    @Test
    fun `get list of games`() {
        val repo = JdbcGamesRepo(dataSource)
        val dev1 = "developer1"
        val dev2 = "developer2"
        val genres12 = listOf("genre1", "genre2")
        val genres23 = listOf("genre2", "genre3")

        val gameId1 = repo.insert("name1", dev1, genres12)
        val game1 = repo.getGameById(gameId1)
        val gameId2 = repo.insert("name2", dev1, genres23)
        val game2 = repo.getGameById(gameId2)
        val gameId3 = repo.insert("name3", dev1, genres12)
        val game3 = repo.getGameById(gameId3)
        val gameId4 = repo.insert("name4", dev2, genres23)
        val game4 = repo.getGameById(gameId4)

        val list1 = repo.getListOfGames(genres12, dev1, 5, 0)
        assertTrue { list1.containsAll(listOf(game1, game2, game3, game4)) && list1.size == 4 }

        val list2 = repo.getListOfGames(genres23, dev1, 5, 0)
        assertTrue { list2.containsAll(listOf(game1, game2, game3, game4)) && list2.size == 4 }

        val list3 = repo.getListOfGames(listOf(genres12[0].uppercase()), dev2, 5, 0)
        assertTrue { list3.containsAll(listOf(game1, game3, game4)) && list3.size == 3 }

        val list4 = repo.getListOfGames(listOf(genres23[1]), dev2, 5, 0)
        assertTrue { list4.containsAll(listOf(game2, game4)) && list4.size == 2 }

        val list5 = repo.getListOfGames(listOf(genres12[0]), dev1, 5, 0)
        assertTrue { list5.containsAll(listOf(game1, game2, game3)) && list5.size == 3 }

        val list6 = repo.getListOfGames(listOf("genre99"), "developer99", 5, 0)
        assertTrue { list6.isEmpty() }
    }

    @Test
    fun `get list of games with paging`() {
        val repo = JdbcGamesRepo(dataSource)
        val dev1 = "developer1"
        val dev2 = "developer2"
        val genres12 = listOf("genre1", "genre2")
        val genres23 = listOf("genre2", "genre3")

        val gameId1 = repo.insert("name1", dev1, genres12)
        val game1 = repo.getGameById(gameId1)
        val gameId2 = repo.insert("name2", dev1, genres23)
        val game2 = repo.getGameById(gameId2)
        val gameId3 = repo.insert("name3", dev1, genres12)
        val game3 = repo.getGameById(gameId3)
        val gameId4 = repo.insert("name4", dev2, genres23)
        val game4 = repo.getGameById(gameId4)

        val list1 = repo.getListOfGames(genres12, dev1, 2, 0)
        assertTrue { list1.containsAll(listOf(game1, game2)) && list1.size == 2 }

        val list2 = repo.getListOfGames(listOf(genres12[0]), dev1, 2, 2)
        assertTrue { list2.containsAll(listOf(game3)) && list2.size == 1 }

        val list3 = repo.getListOfGames(genres23, dev1, 2, 0)
        assertTrue { list3.containsAll(listOf(game1, game2)) && list3.size == 2 }

        val list4 = repo.getListOfGames(genres23, dev1, 2, 2)
        assertTrue { list4.containsAll(listOf(game3, game4)) && list4.size == 2 }

        val list5 = repo.getListOfGames(genres23, dev2, 5, 6)
        assertTrue { list5.isEmpty() }
    }
}