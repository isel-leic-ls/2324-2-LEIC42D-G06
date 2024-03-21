package pt.isel.ls.services.jdbc.games

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.AppException
import pt.isel.ls.repo.jdbc.JdbcGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.services.GamesServices
import pt.isel.ls.utils.FIRST_GAME_ID
import pt.isel.ls.utils.FIRST_PLAYER_ID
import pt.isel.ls.utils.generatePlayerDetails
import kotlin.test.*


class ServiceJDBCGamesTests {
    private val dataSource = PGSimpleDataSource().apply {
        setUrl(System.getenv("JDBC_DATABASE_URL"))
    }

    private val pRepo = MemPlayersRepo() //TODO change to JdbcPlayersRepo

    @BeforeTest
    fun setup() {
        dataSource.connection.use {
            val stmt0 = it.prepareStatement("DELETE FROM SessionPlayer")
            stmt0.executeUpdate()
            val stmt1 = it.prepareStatement("DELETE FROM Session")
            stmt1.executeUpdate()
            val stmt2 = it.prepareStatement("DELETE FROM Game")
            stmt2.executeUpdate()
        }
        generatePlayerDetails().let { (n, e, t,p) -> pRepo.createPlayer(n, e, t,p) }
    }

    @Test
    fun `test createGame successfully`() {
        val service = GamesServices(JdbcGamesRepo(dataSource), pRepo)
        val foundPlayer = pRepo.getPlayer(FIRST_PLAYER_ID) ?: throw Exception("Error occurred")
        val token = foundPlayer.token
        val gameId1 =
            service.createGame(token, "CS", "valveDev", listOf("fps"))
        val gameId2 =
            service.createGame(token, "GTA V", "rockstarGamesDev", listOf("action", "adventure"))

        assertTrue(gameId1 >= FIRST_GAME_ID)
        assertTrue(gameId2 >= FIRST_GAME_ID + 1)
    }

    @Test
    fun `test createGame creating a game with the same name (case-insensitive)`() {
        val service = GamesServices(JdbcGamesRepo(dataSource), pRepo)
        val foundPlayer = pRepo.getPlayer(FIRST_PLAYER_ID) ?: throw Exception("Error occurred")
        val token = foundPlayer.token

        service.createGame(token, "CS", "valveDev", listOf("fps"))

        val nameVariations = listOf("cs", "cS", "Cs", "CS") //case insensitive
        nameVariations.forEach {
            assertFailsWith<AppException.GameAlreadyExists> {
                service.createGame(token, it, "valveDev", listOf("fps"))
            }
        }
    }

    @Test
    fun `test createGame creating a game with invalid data`() {
        val service = GamesServices(JdbcGamesRepo(dataSource), pRepo)
        val foundPlayer = pRepo.getPlayer(FIRST_PLAYER_ID) ?: throw Exception("Error occurred")
        val token = foundPlayer.token

        val invalidData = listOf(
            Triple("", "valveDev", listOf("fps")),
            Triple("CS", "  ", listOf("fps")),
            Triple("CS", "valveDev", listOf("")),
            Triple("CS", "valveDev", listOf("fps", "")),
        )
        invalidData.forEach {
            assertFailsWith<AppException.BadRequestCreateGame> {
                service.createGame(token, it.first, it.second, it.third)
            }
        }
    }

    @Test
    fun `test getDetailsOfGameById and getDetailsOfGameByName`() {
        val service = GamesServices(JdbcGamesRepo(dataSource), pRepo)
        val foundPlayer = pRepo.getPlayer(FIRST_PLAYER_ID) ?: throw Exception("Error occurred")
        val token = foundPlayer.token
        val gName = "CS"
        val gDev = "valveDev"
        val gGenres = listOf("fps", "tactical")

        val gId = service.createGame(token, gName, gDev, gGenres)
        val gameById = service.getDetailsOfGameById(gId)
        val gameByName = service.getDetailsOfGameByName(gName)

        assertEquals(gId, gameById.id)
        assertEquals(gName, gameById.name)
        assertEquals(gDev, gameById.dev)
        assertEquals(gGenres, gameById.genres)

        assertEquals(gId, gameByName.id)
        assertEquals(gName, gameByName.name)
        assertEquals(gDev, gameByName.dev)
        assertEquals(gGenres, gameByName.genres)

        assertEquals(gameById, gameByName)
    }

    @Test
    fun `test getListOfGames with and without limit and skip`() {
        val service = GamesServices(JdbcGamesRepo(dataSource), pRepo)
        val foundPlayer = pRepo.getPlayer(FIRST_PLAYER_ID) ?: throw Exception("Error occurred")
        val token = foundPlayer.token

        val g1 =
            service.createGame(token, "CS", "valveDev", listOf("fps", "tactical"))
        val g2 =
            service.createGame(token, "GTA V", "rockstarGamesDev", listOf("action", "adventure"))
        val g3 =
            service.createGame(token, "FIFA 20", "eaSportsDev", listOf("sports"))
        val g4 =
            service.createGame(token, "WWE 2K20", "eaSportsDev", listOf("sports"))

        val games1 = service.getListOfGames(listOf("fps", "tactical"), "valveDev")
        assertEquals(1, games1.size)
        assertEquals(g1, games1[0].id)

        val games2 = service.getListOfGames(listOf("action", "adventure"), "", 10, 0)
        assertEquals(1, games2.size)
        assertEquals(g2, games2[0].id)

        val games3 = service.getListOfGames(listOf("sports"), "eaSportsDev")
        assertEquals(2, games3.size)
        assertEquals(g3, games3[0].id)
        assertEquals(g4, games3[1].id)

        val games4 = service.getListOfGames(listOf("SPORTS"), "eaSportsDev", 5, 1)
        assertEquals(1, games4.size)
        assertEquals(g4, games4[0].id)

        val games5 = service.getListOfGames(listOf("sports"), "easportsdev", 1, 0)
        assertEquals(1, games5.size)
        assertEquals(g3, games5[0].id)

        val games6 = service.getListOfGames(listOf(), "ROCKSTARGAMESDEV", 5, 1)
        assertEquals(0, games6.size)

        val games7 = service.getListOfGames(listOf(), "rockstarGamesDev", 5, 2)
        assertEquals(0, games7.size)

        val listOfParamsPair = listOf(Pair(-1, 5), Pair(0, 5), Pair(3, -1))
        listOfParamsPair.forEach {
            assertFailsWith<IllegalStateException> {
                service.getListOfGames(listOf(), "rockstarGamesDev", it.first, it.second)
            }
        }
    }
}