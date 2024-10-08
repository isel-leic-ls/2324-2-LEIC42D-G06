package pt.isel.ls.api.mem.player

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.api.PlayerUris
import pt.isel.ls.api.mem.mockServices.PlayerRoutesMock
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.utils.FIRST_PLAYER_ID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class ApiMemPlayerTests {
    //repositories
    private val playersRepo = MemPlayersRepo()

    //services
    private val playerServices = PlayerServices(playersRepo)

    //private val serviceRoutes = PlayerRoutes(playerServices)
    private val mockServiceRoutes = PlayerRoutesMock()

    //server
    private val server = mockServiceRoutes.routes.asServer(Jetty(8080))

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

        val response = mockServiceRoutes.routes(request)
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

        val response = mockServiceRoutes.routes(request)
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

        val response = mockServiceRoutes.routes(request)
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

        val response = mockServiceRoutes.routes(request)
        assertEquals(400, response.status.code)
    }

    @Test
    fun `retrieving an existing player`() {
        val request = Request(Method.GET, PlayerUris.GET.replace("{pid}", FIRST_PLAYER_ID.toString()))
        val response = mockServiceRoutes.routes(request)
        assertEquals(200, response.status.code)
    }

    @Test
    fun `retrieving a non existent player`() {
        val request = Request(Method.GET, PlayerUris.GET.replace("{pid}", "100"))
        val response = mockServiceRoutes.routes(request)
        assertEquals(404, response.status.code)
    }

    @Test
    fun `get a player by username`() {
        val url = "/api/players?name=test"
        val request = Request(Method.GET, url)
        val response = mockServiceRoutes.routes(request)
        assertEquals(200, response.status.code)
    }

    @Test
    fun `get a player by username invalid route`() {
        val url = "/api/plsayers?name=Vasco"
        val request = Request(Method.GET, url)
        val response = mockServiceRoutes.routes(request)
        assertEquals(404, response.status.code)
    }


}