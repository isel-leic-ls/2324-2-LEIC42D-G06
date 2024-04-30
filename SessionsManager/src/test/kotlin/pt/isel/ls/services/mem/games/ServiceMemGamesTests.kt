package pt.isel.ls.services.mem.games

import pt.isel.ls.AppException
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.services.GamesServices
import pt.isel.ls.utils.FIRST_GAME_ID
import pt.isel.ls.utils.FIRST_PLAYER_ID
import pt.isel.ls.utils.generatePlayerDetails
import kotlin.test.*


class ServiceMemGamesTests {
    private val pRepo = MemPlayersRepo()

    @BeforeTest
    fun setup() {
        generatePlayerDetails().let { (n, e, t, p) -> pRepo.createPlayer(n, e, t, p) }
    }

    @Test
    fun `test createGame successfully`() {
        val service = GamesServices(MemGamesRepo(), pRepo)
        val foundPlayer = pRepo.getPlayer(FIRST_PLAYER_ID)
        val token = foundPlayer.token
        val gameId1 =
            service.createGame(token, "CS", "valveDev", listOf("fps"))
        val gameId2 =
            service.createGame(token, "GTA V", "rockstarGamesDev", listOf("action", "adventure"))

        assertEquals(gameId1, FIRST_GAME_ID)
        assertEquals(gameId2, FIRST_GAME_ID + 1)
    }

    @Test
    fun `test createGame creating a game with the same name (case-insensitive)`() {
        val service = GamesServices(MemGamesRepo(), pRepo)
        val foundPlayer = pRepo.getPlayer(FIRST_PLAYER_ID)
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
        val service = GamesServices(MemGamesRepo(), pRepo)
        val foundPlayer = pRepo.getPlayer(FIRST_PLAYER_ID)
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
        val service = GamesServices(MemGamesRepo(), pRepo)
        val foundPlayer = pRepo.getPlayer(FIRST_PLAYER_ID)
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
        val service = GamesServices(MemGamesRepo(), pRepo)
        val foundPlayer = pRepo.getPlayer(FIRST_PLAYER_ID)
        val token = foundPlayer.token

        val g1 =
            service.createGame(token, "CS", "valveDev", listOf("fps", "tactical"))
        val g2 =
            service.createGame(token, "GTA V", "rockstarGamesDev", listOf("action", "adventure"))
        val g3 =
            service.createGame(token, "FIFA 20", "eaSportsDev", listOf("sports"))
        val g4 =
            service.createGame(token, "WWE 2K20", "eaSportsDev", listOf("sports"))

        val (games1, _) = service.getListOfGames(listOf("fps", "tactical"), "valveDev")
        assertEquals(1, games1.size)
        assertEquals(g1, games1[0].id)

        val (games2, _) = service.getListOfGames(listOf("action     ", "adventure"), "rockstarGamesDev")
        assertEquals(1, games2.size)
        assertEquals(g2, games2[0].id)

        val (games3, _) = service.getListOfGames(listOf(" sports "), "eaSportsDev")
        assertEquals(2, games3.size)
        assertEquals(g3, games3[0].id)
        assertEquals(g4, games3[1].id)

        val (games4, _) = service.getListOfGames(listOf("SPORTS"), "eaSportsDev", 5, 1)
        assertEquals(1, games4.size)
        assertEquals(g4, games4[0].id)

        val (games5, _) = service.getListOfGames(listOf("sports"), "easportsdev     ", 1, 0)
        assertEquals(1, games5.size)
        assertEquals(g3, games5[0].id)

        val (games6, _) = service.getListOfGames(listOf("rts"), "   ROCKSTARGAMESDEV", 5, 1)
        assertEquals(0, games6.size)

        val (games7, _) = service.getListOfGames(listOf("fps"), "rocKstarGAMEsDeV", 5, 4)
        assertEquals(0, games7.size)

        val (games8, _) = service.getListOfGames(listOf("   fps    "), "   ", 5, 0)
        assertEquals(1, games8.size)

        val (games9, _) = service.getListOfGames(listOf("   "), "eaSportsDEV", 1, 1)
        assertEquals(1, games9.size)

        val (games10, _) = service.getListOfGames(listOf(), "", 2, 1)
        assertEquals(2, games10.size)

        val listOfParamsPair = listOf(Pair(-1, 5), Pair(0, 5), Pair(3, -1))
        listOfParamsPair.forEach {
            assertFailsWith<IllegalArgumentException> {
                service.getListOfGames(listOf("sports"), "rockstarGamesDev", it.first, it.second)
            }
        }
    }
}