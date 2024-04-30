package pt.isel.ls.api.mem.games

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.api.GameUrisObj
import pt.isel.ls.api.mem.mockServices.GamesRoutesMock
import pt.isel.ls.api.model.GameOutputModel
import pt.isel.ls.api.model.GamesListOutputModel
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.utils.FIRST_GAME_ID
import pt.isel.ls.utils.FIRST_PLAYER_ID
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class ApiMemGamesTests {
    //repositories
    private val playersRepo = MemPlayersRepo()
    private val gamesRepo = MemGamesRepo()

    //service
    private val mockServiceRoutes = GamesRoutesMock()

    //server
    private val server = mockServiceRoutes.routes.asServer(Jetty(8080))

    @BeforeTest
    fun setup() {
        val hardcodedToken = "f4b9b2b1-3c6f-4f5e-9d5f-77e8b0d8b5f6"
        playersRepo.createPlayer("joao", "joaojoao@gmail.com", hardcodedToken, "passwordJP")
        gamesRepo.insert("GTA V", "Rockstar Games", listOf("Action"))
        server.start()
    }

    @AfterTest
    fun stopServer() {
        server.stop()
    }

    @Test
    fun `create a game`() {
        val name = "FIFA"
        val company = "EA"
        val genres = listOf("Sports", "Football")
        val foundPlayer = playersRepo.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
            {
                "name": "$name",
                "developer": "$company",
                "genres": ["${genres[0]}", "${genres[1]}"]
            }
        """.trimIndent()

        val request = Request(Method.POST, GameUrisObj.CREATE)
            .body(requestBody)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")

        val response = mockServiceRoutes.routes(request)
        assertEquals(201, response.status.code)

        val outputModel = Json.decodeFromString<GameOutputModel>(response.bodyString())
        assertEquals(FIRST_GAME_ID + 1, outputModel.gId)
    }

    @Test
    fun `create a game with input to be trimmed`() {
        val name = "  FIFA  "
        val company = "  EA  "
        val genres = listOf("  Sports  ", "  Football  ", "  Online Competition  ")
        val foundPlayer = playersRepo.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
            {
                "name": "$name",
                "developer": "$company",
                "genres": ["${genres[0]}", "${genres[1]}", "${genres[2]}"]
            }
        """.trimIndent()

        val request = Request(Method.POST, GameUrisObj.CREATE)
            .body(requestBody)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")

        val response = mockServiceRoutes.routes(request)
        assertEquals(201, response.status.code)

        val outputModel = Json.decodeFromString<GameOutputModel>(response.bodyString())
        assertEquals(FIRST_GAME_ID + 1, outputModel.gId)
    }

    @Test
    fun `get game by id`() {
        val foundPlayer = playersRepo.getPlayer(FIRST_PLAYER_ID)

        val request = Request(Method.GET, GameUrisObj.GET_BY_ID.replace("{gid}", FIRST_GAME_ID.toString()))
            .header("Authorization", "Bearer ${foundPlayer.token}")

        val response = mockServiceRoutes.routes(request)
        assertEquals(200, response.status.code)
    }

    @Test
    fun `get game by name`() {
        val foundPlayer = playersRepo.getPlayer(FIRST_PLAYER_ID)
        val name = "GTA V"
        val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())

        val request = Request(Method.GET, GameUrisObj.GET_BY_NAME.replace("{gname}", encodedName))
            .header("Authorization", "Bearer ${foundPlayer.token}")

        val response = mockServiceRoutes.routes(request)
        assertEquals(200, response.status.code)
    }

    @Test
    fun `get list of games by partial name`() {
        val encodedName = URLEncoder.encode("gt", StandardCharsets.UTF_8.toString())
        val requestBody =
            """
            {
                "partialName": "gt"
            }
        """.trimIndent()
        val request =
            Request(Method.GET, GameUrisObj.GET_GAMES_BY_NAME + "?gname=$encodedName?skip=0&limit=8")
                .body(requestBody)
                .header("Content-Type", "application/json")

        val response = mockServiceRoutes.routes(request)
        assertEquals(200, response.status.code)
    }

    @Test
    fun `get list of one game`() {
        val request = Request(
            Method.GET, GameUrisObj.GET_GAMES_BY_GENRES_DEV + "?genres=Action&developer=Rockstar Games"
        )

        val response = mockServiceRoutes.routes(request)
        assertEquals(200, response.status.code)
        assertEquals(1, Json.decodeFromString<GamesListOutputModel>(response.bodyString()).games.size)
    }
}