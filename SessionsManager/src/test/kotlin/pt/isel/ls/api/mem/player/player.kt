package pt.isel.ls.api.mem.player

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.api.PlayerRoutes
import pt.isel.ls.api.PlayerUris
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
        val requestBody = """
        {
            "name": "$name",
            "email": "$email",
            "password": "$password"
        }
        """.trimIndent()

        val request = Request(Method.POST, PlayerUris.CREATE)
            .header("Content-Type", "application/json")
            .body(requestBody)

        val response = serviceRoutes.routes(request)
        assertEquals(201, response.status.code)

    }

    @Test
    fun `creating a player without name that should return 400 status code`() {
        val email = "pedrodiz@gmail.com"
        val password = "pedro123"
        val requestBody = """
            {
                "name": ,
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()

        val request = Request(Method.POST, PlayerUris.CREATE)
            .body(requestBody)
            .header("Content-Type", "application/json")

        val response = serviceRoutes.routes(request)
        assertEquals(400, response.status.code)
    }

    @Test
    fun `creating a player without email that should return 400 status code`() {
        val name = "Pedro"
        val password = "pedro123"
        val requestBody = """
            {
                "name": "$name",
                "email": ,
                "password": "$password"
            }
        """.trimIndent()

        val request = Request(Method.POST, PlayerUris.CREATE)
            .body(requestBody)
            .header("Content-Type", "application/json")

        val response = serviceRoutes.routes(request)
        assertEquals(400, response.status.code)

    }

    @Test
    fun `creating a player without token that should return 400 status code`() {
        val name = "Pedro"
        val email = "pedrodiz@gmail.com"
        val requestBody = """
            {
                "name": "$name",
                "email": "$email,
                "password": 
            }
        """.trimIndent()

        val request = Request(Method.POST, PlayerUris.CREATE)
            .body(requestBody)
            .header("Content-Type", "application/json")

        val response = serviceRoutes.routes(request)
        assertEquals(400, response.status.code)
    }

    @Test
    fun `retrieving an existing player`() {
        val request = Request(Method.GET, PlayerUris.GET.replace("{pid}", FIRST_PLAYER_ID.toString()))
        val response = serviceRoutes.routes(request)
        assertEquals(200, response.status.code)
    }

    @Test
    fun `retrieving a non existent player`() {
        val request = Request(Method.GET, PlayerUris.GET.replace("{pid}", "100"))
        val response = serviceRoutes.routes(request)
        assertEquals(400, response.status.code)
    }
}