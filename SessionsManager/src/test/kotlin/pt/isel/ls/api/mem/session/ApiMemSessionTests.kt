package pt.isel.ls.api.mem.session

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.junit.Test
import pt.isel.ls.api.Problem
import pt.isel.ls.api.Session
import pt.isel.ls.api.SessionRoutes
import pt.isel.ls.api.model.CreateSessionOutputModel
import pt.isel.ls.api.model.SessionRetrievalOutputModel
import pt.isel.ls.domain.createSessionDTO
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.repo.mem.MemSessionRepo
import pt.isel.ls.services.Services
import pt.isel.ls.utils.DATE_FORMATTER
import pt.isel.ls.utils.FIRST_GAME_ID
import pt.isel.ls.utils.FIRST_PLAYER_ID
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
    // initialize the repositories
    private val playersRepo = MemPlayersRepo()
    private val gamesRepo = MemGamesRepo()
    private val sessionRepo = MemSessionRepo()

    // create the services
    private val services = Services(gamesRepo, playersRepo, sessionRepo)

    // setup the server with the session routes only
    private val serviceRoutes = SessionRoutes(services.sessionService)
    private val server = serviceRoutes.routes.asServer(Jetty(8080))

    @BeforeTest
    fun setup() {
        // create some players
        playersRepo.createPlayer("joao", "joao@hotmail.com", UUID.randomUUID().toString(),"joao123")
        playersRepo.createPlayer("pedro", "pedro@gmail.com", UUID.randomUUID().toString(),"pedro123")

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
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val capacity = 2
        val fPlayer = playersRepo.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
        {
            "gid": ${gid},
            "capacity": ${capacity},
            "date": "$date"
        }
        """.trimIndent()

        //act
        val request = Request(Method.POST, Session.CREATE)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${fPlayer.token}")
            .body(requestBody)

        val response = serviceRoutes.routes(request)
        // assert the session was created
        assertEquals(201, response.status.code)

        // assert the session id is a number bigger than 0
        val outputModel = Json.decodeFromString<CreateSessionOutputModel>(response.bodyString())

        assertTrue(outputModel.sid.toInt() > 0)
    }

    @Test
    fun `creating a session with past date should return status code 400`() {
        // arrange
        val gid = gamesRepo.getGameByName("FIFA").id
        val date = LocalDateTime.now().minusDays(1).format(DATE_FORMATTER)
        val capacity = 2
        val fPlayer = playersRepo.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
        {
            "gid": ${gid},
            "capacity": ${capacity},
            "date": "$date"
        }
        """.trimIndent()

        val eProblem = Problem("Invalid date $date")

        // act
        val request = Request( Method.POST, Session.CREATE)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${fPlayer.token}")
            .body(requestBody)

        val response = serviceRoutes.routes(request)
        // assert the status code is 400
        assertEquals(response.status.code, Status.BAD_REQUEST.code)

        // assert the description is date format is invalid
        val problem = Json.decodeFromString<Problem>(response.bodyString())

        assertEquals(eProblem, problem)
    }

    @Test
    fun `creating a session with invalid capacity should return status code 400`() {
        //arrange
        val gid = gamesRepo.getGameByName("FIFA").id
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val capacity = -1
        val fPlayer = playersRepo.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
        {
            "gid": ${gid},
            "capacity": ${capacity},
            "date": "$date"
        }
        """.trimIndent()

        val eProblem = Problem("Invalid capacity $capacity")

        // act
        val request = Request(Method.POST, Session.CREATE)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${fPlayer.token}")
            .body(requestBody)

        val response = serviceRoutes.routes(request)
        // assert the status code is 400
        assertEquals(response.status.code, Status.BAD_REQUEST.code)

        // assert the description is date format is invalid
        val problem = Json.decodeFromString<Problem>(response.bodyString())

        assertEquals(eProblem, problem)

    }

    @Test
    fun `creating a session with badly formatted date should return status code 400`() {
        // arrange
        val gid = gamesRepo.getGameByName("FIFA").id
        val date = "202300310310310310"
        val capacity = 2
        val fPlayer = playersRepo.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
        {
            "gid": ${gid},
            "capacity": ${capacity},
            "date": "$date"
        }
        """.trimIndent()

        val eProblem = Problem("Invalid date format $date")

        // act

        val request = Request(Method.POST, Session.CREATE)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${fPlayer.token}")
            .body(requestBody)

        val response = serviceRoutes.routes(request)
        // assert the status code is 400
        assertEquals(response.status.code, Status.BAD_REQUEST.code)

        // assert the description is date format is invalid
        val problem = Json.decodeFromString<Problem>(response.bodyString())

        assertEquals(eProblem, problem)
    }

    @Test
    fun `retrieving a nonexistant session should return status code 404`() {
        // arrange
        val id = 10101131 // using a bigger number results in status code 500 (?????????)
        // act..
        val request = Request(Method.GET, Session.GET.replace("{sid}", id.toString()))
        val response = serviceRoutes.routes(request)
        // assert the status code is 404 (not found)
        assertEquals(response.status.code, Status.NOT_FOUND.code)
    }

    @Test
    fun `retrieving a session by passing in a string in the uri should return status code 400`() {
        // arrange
        val id = "bolacha"

        // act
        val request = Request(Method.GET, Session.GET.replace("{sid}", id))
        val response = serviceRoutes.routes(request)
        // assert the status code is 400 (bad req)
        assertEquals(response.status.code, Status.BAD_REQUEST.code)

    }

    @Test
    fun `retrieving a session`() {
        //arrange
        val capacity = 2
        val gid = FIRST_GAME_ID
        val date = LocalDateTime.now().plusDays(1).format(DATE_FORMATTER)
        val sid = sessionRepo.createSession(createSessionDTO(capacity, date, gid, listOf(FIRST_PLAYER_ID)))

        // act
        val request = Request(Method.GET, Session.GET.replace("{sid}", sid.toString()))
        val response = serviceRoutes.routes(request)
        // assert the status code is 200 (ok)
        assertEquals(response.status.code, Status.OK.code)

        val outputModel = Json.decodeFromString<SessionRetrievalOutputModel>(response.bodyString())
        // assert the session structure
        assertEquals(sessionRepo.getSession(sid), outputModel.session)
    }
}