package pt.isel.ls.services.mem.games

import pt.isel.ls.repo.DomainException
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.services.GamesServices
import pt.isel.ls.utils.INITIAL_GAME_ID
import pt.isel.ls.utils.generatePlayerDetails
import kotlin.test.*


class ServiceMemGamesTests {
    private val pRepo = MemPlayersRepo()

    @BeforeTest
    fun setup() {
        generatePlayerDetails().let { (n, e, t) -> pRepo.createPlayer(n, e, t) }
    }

    @Test
    fun `test createGame successfully`() {
        val service = GamesServices(MemGamesRepo(), pRepo)
        val foundPlayer = pRepo.getPlayer(1) ?: throw Exception("Error occurred")
        val token = foundPlayer.token
        val gameId1 = service.createGame(token, "CS", "valveDev", listOf("fps"))
        val gameId2 = service.createGame(token, "GTA V", "rockstarGamesDev", listOf("action", "adventure"))

        assertEquals(gameId1, INITIAL_GAME_ID)
        assertEquals(gameId2, INITIAL_GAME_ID + 1)
    }

    @Test
    fun `test createGame creating a game with the same name (case-insensitive)`() {
        val service = GamesServices(MemGamesRepo(), pRepo)
        val foundPlayer = pRepo.getPlayer(1) ?: throw Exception("Error occurred")
        val token = foundPlayer.token

        service.createGame(token, "CS", "valveDev", listOf("fps"))

        val nameVariations = listOf("cs", "cS", "Cs", "CS") //case insensitive
        nameVariations.forEach {
            assertFailsWith<DomainException.GameAlreadyExists> {
                service.createGame(token, it, "valveDev", listOf("fps"))
            }
        }
    }

    @Test
    fun `test createGame creating a game with invalid data`() {
        val service = GamesServices(MemGamesRepo(), pRepo)
        val foundPlayer = pRepo.getPlayer(1) ?: throw Exception("Error occurred")
        val token = foundPlayer.token

        val invalidData = listOf(
            Triple("", "valveDev", listOf("fps")),
            Triple("CS", "  ", listOf("fps")),
            Triple("CS", "valveDev", listOf("")),
            Triple("CS", "valveDev", listOf("fps", "")),
        )
        invalidData.forEach {
            assertFailsWith<DomainException.BadRequestCreateGame> {
                service.createGame(token, it.first, it.second, it.third)
            }
        }
    }

    @Test
    fun `test getDetailsOfGameById and getDetailsOfGameByName`() {
        val service = GamesServices(MemGamesRepo(), pRepo)
        val foundPlayer = pRepo.getPlayer(1) ?: throw Exception("Error occurred")
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
        assertSame(gameById, gameByName)
    }

    @Test
    fun `test getListOfGames with and without limit and skip`() {
        val service = GamesServices(MemGamesRepo(), pRepo)
        val foundPlayer = pRepo.getPlayer(1) ?: throw Exception("Error occurred")
        val token = foundPlayer.token
        val g1 = service.createGame(token, "CS", "valveDev", listOf("fps", "tactical"))
        val g2 = service.createGame(token, "GTA V", "rockstarGamesDev", listOf("action", "adventure"))
        val g3 = service.createGame(token, "FIFA 20", "eaSportsDev", listOf("sports"))
        val g4 = service.createGame(token, "WWE 2K20", "eaSportsDev", listOf("sports"))

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

        val games4 = service.getListOfGames(listOf("sports"), "eaSportsDev", 5, 1)
        assertEquals(1, games4.size)
        assertEquals(g4, games4[0].id)

        val games5 = service.getListOfGames(listOf("sports"), "eaSportsDev", 1, 0)
        assertEquals(1, games5.size)
        assertEquals(g3, games5[0].id)

        val games6 = service.getListOfGames(listOf(), "rockstarGamesDev", 5, 1)
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