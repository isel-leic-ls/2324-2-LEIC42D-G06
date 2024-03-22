import kotlinx.serialization.json.Json
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.api.PlayerRoutes
import pt.isel.ls.api.model.PlayerOutputModel
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.services.PlayerServices

import pt.isel.ls.utils.FIRST_PLAYER_ID
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiMemPlayerTests {
    private val client = HttpClient.newBuilder().build()

    //repositories
    private val playersRepo = MemPlayersRepo()

    //services
    private val playerServices = PlayerServices(playersRepo)
    private val serviceRoutes = PlayerRoutes(playerServices)

    //server
    private val server = serviceRoutes.routes.asServer(Jetty(8080))

    @BeforeTest
    fun setup() {
        playerServices.createPlayer("Pedro", "pedrodiz@gmail.com", "pedro123")
        server.start()
    }

    @AfterTest
    fun stopServer() {
        server.stop()
    }

    @Test
    fun `create a player`() {
        val name = "Vasco"
        val email = "vascobranco@gmail.com"
        val password = "vasco123"
        //val player = playerServices.getPlayer(FIRST_PLAYER_ID)
        val requestBody = """
        {
            "name": "$name",
            "email": "$email",
            "password": "$password"
        }
        """.trimIndent()


        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/players"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        assertEquals(201, response.statusCode())

    }

    @Test
    fun `creating a player without name that should return 400 status code`() {
        val email = "pedrodiz@gmail.com"
        val player = playerServices.getPlayer(FIRST_PLAYER_ID)
        val requestBody = """
            {
                "name": ,
                "email": "$email",
                "token": "${player.token}"
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/players"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${player?.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        assertEquals(400, response.statusCode())
    }

    @Test
    fun `creating a player without email that should return 400 status code`() {
        val name = "Pedro"
        //val email = "pedrodiz@gmail.com"
        val player = playerServices.getPlayer(FIRST_PLAYER_ID)
        val requestBody = """
            {
                "name": "$name",
                "email": ,
                "token": "${player.token}"
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/players"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${player?.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        assertEquals(400, response.statusCode())

    }

    @Test
    fun `creating a player without token that should return 400 status code`() {
        val name = "Pedro"
        val email = "pedrodiz@gmail.com"
        val player = playerServices.getPlayer(FIRST_PLAYER_ID)
        val requestBody = """
            {
                "name": "$name",
                "email": "$email,
                "token": 
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/players"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${player?.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        assertEquals(400, response.statusCode())
    }

    @Test
    fun `retrieving an existing player`() {
        val player = playerServices.getPlayer(FIRST_PLAYER_ID)
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/players/$FIRST_PLAYER_ID"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${player.token}")
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        assertEquals(200, response.statusCode())
    }

    @Test
    fun `retrieving a non existent player`() {
        val player = playerServices.getPlayer(FIRST_PLAYER_ID)
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/players/100"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${player.token}")
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        assertEquals(400, response.statusCode())
    }
}