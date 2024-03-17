package pt.isel.ls.api.mem.session

import kotlinx.serialization.json.Json
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.junit.Test
import pt.isel.ls.api.SessionRoutes
import pt.isel.ls.api.model.CreateSessionOutputModel
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.repo.mem.MemSessionRepo
import pt.isel.ls.services.Services
import pt.isel.ls.utils.FIRST_PLAYER_ID
import pt.isel.ls.utils.dateFormatter
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApiMemSessionTests {
    private val client = HttpClient.newBuilder().build()

    // initialize the repositories
    val playersRepo = MemPlayersRepo()
    val gamesRepo = MemGamesRepo()
    val servicesRepo = MemSessionRepo()

    // create the services
    val services = Services(gamesRepo, playersRepo, servicesRepo)
    val serviceRoutes = SessionRoutes(services.sessionService) // server only using the session routes for now...

    // setup the server
    val server = serviceRoutes.routes.asServer(Jetty(8080))

    @BeforeTest
    fun setup() {
        // create some players
        playersRepo.createPlayer("joao", "joao@hotmail.com", UUID.randomUUID().toString())
        playersRepo.createPlayer("pedro", "pedro@gmail.com", UUID.randomUUID().toString())

        // create some games
        gamesRepo.insert("FIFA", "EA", listOf("Sports", "Football"))

        server.start()
    }

    @AfterTest
    fun teardown() {
        server.stop()
    }

    @Test
    fun `creating a session`() {
        // arrange
        val gid = gamesRepo.getGameByName("FIFA").id
        val date = LocalDateTime.now().plusDays(1).format(dateFormatter)
        val capacity = 2
        val fPlayer = playersRepo.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
        {
            "gid": ${gid},
            "capacity": ${capacity},
            "date": "$date"
        }
    """.trimIndent()

        // act
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/sessions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${fPlayer?.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        // assert the session was created
        assertEquals(201, response.statusCode())

        // assert the session id is a number bigger than 0
        val outputModel = Json.decodeFromString<CreateSessionOutputModel>(response.body())

        assertTrue(outputModel.sid.toInt() > 0)
    }
}