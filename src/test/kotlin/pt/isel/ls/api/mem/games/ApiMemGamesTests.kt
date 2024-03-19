package pt.isel.ls.api.mem.games

import kotlinx.serialization.json.Json
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.api.GamesRoutes
import pt.isel.ls.api.model.GameOutputModel
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.services.GamesServices
import pt.isel.ls.utils.FIRST_GAME_ID
import pt.isel.ls.utils.FIRST_PLAYER_ID
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class ApiMemGamesTests {
    private val client = HttpClient.newBuilder().build()

    //repositories
    private val playersRepo = MemPlayersRepo()
    private val gamesRepo = MemGamesRepo()

    //services
    private val gameServices = GamesServices(gamesRepo, playersRepo)
    private val serviceRoutes = GamesRoutes(gameServices)

    //server
    private val server = serviceRoutes.routes.asServer(Jetty(8080))

    @BeforeTest
    fun setup() {
        playersRepo.createPlayer("joao", "joao@gmail.com", UUID.randomUUID().toString())
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

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/games"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer?.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(201, response.statusCode())

        val outputModel = Json.decodeFromString<GameOutputModel>(response.body())

        assertEquals(FIRST_GAME_ID, outputModel.gId)
    }

    //TODO: Test get game by id

    //TODO: Test get game by name

    //TODO: Test get list of games
}