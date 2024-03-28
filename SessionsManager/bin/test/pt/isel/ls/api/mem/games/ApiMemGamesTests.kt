package pt.isel.ls.api.mem.games

import kotlinx.serialization.json.Json
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.api.GamesRoutes
import pt.isel.ls.api.model.GameOutputModel
import pt.isel.ls.api.model.GamesListOutputModel
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.services.GamesServices
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.utils.FIRST_GAME_ID
import pt.isel.ls.utils.FIRST_PLAYER_ID
import pt.isel.ls.utils.LIMIT_DEFAULT
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import kotlin.test.*


class ApiMemGamesTests {
    private val client = HttpClient.newBuilder().build()

    //repositories
    private val playersRepo = MemPlayersRepo()
    private val gamesRepo = MemGamesRepo()

    //services
    private val gamesServices = GamesServices(gamesRepo, playersRepo)
    private val playerServices = PlayerServices(playersRepo)
    private val serviceRoutes = GamesRoutes(gamesServices)

    //server
    private val server = serviceRoutes.routes.asServer(Jetty(8080))

    @BeforeTest
    fun setup() {
        playerServices.createPlayer("joao", "joaojoao@gmail.com", "joao123")
        val fPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)
        gamesServices.createGame(fPlayer.token, "GTA V", "Rockstar Games", listOf("Action"))
        server.start()
    }

    @AfterTest
    fun stopServer() {
        server.stop()
    }

    private fun createMoreGames(t: String) {
        gamesServices.createGame(t, "FIFA 23", "EA", listOf("Sports", "Football"))
        gamesServices.createGame(t, "EAFC 24", "EA", listOf("Sports", "Football"))
        gamesServices.createGame(t, "GTA", "Rockstar Games", listOf("Action"))
        gamesServices.createGame(t, "GTA II", "Rockstar Games", listOf("Action"))
        gamesServices.createGame(t, "GTA III", "Rockstar Games", listOf("Action"))
        gamesServices.createGame(t, "GTA Vice City", "Rockstar Games", listOf("Action"))
        gamesServices.createGame(t, "GTA San Andreas", "Rockstar Games", listOf("Action"))
        gamesServices.createGame(t, "GTA Liberty City Stories", "Rockstar Games", listOf("Action"))
        gamesServices.createGame(t, "GTA Vice City Stories", "Rockstar Games", listOf("Action"))
        gamesServices.createGame(t, "GTA IV", "Rockstar Games", listOf("Action"))
        gamesServices
            .createGame(t, "Red Dead Redemption", "Rockstar Games", listOf("Action", "Adventure"))
        gamesServices
            .createGame(t, "Red Dead Redemption 2", "Rockstar Games", listOf("Action", "Adventure"))
        gamesServices.createGame(t, "Bully", "Rockstar Games", listOf("Action"))
        gamesServices.createGame(t, "Call of Duty", "Activision", listOf("Action", "Shooter"))
        gamesServices.createGame(t, "Call of Duty 2", "Activision", listOf("Action", "Shooter"))
        gamesServices.createGame(t, "Call of Duty 3", "Activision", listOf("Action", "Shooter"))
    }

    @Test
    fun `create a game`() {
        val name = "FIFA"
        val company = "EA"
        val genres = listOf("Sports", "Football")
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
            {
                "name": "$name",
                "developer": "$company",
                "genres": ["${genres[0]}", "${genres[1]}"]
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(201, response.statusCode())

        val outputModel = Json.decodeFromString<GameOutputModel>(response.body())

        assertEquals(FIRST_GAME_ID + 1, outputModel.gId)
    }

    @Test
    fun `create a game with input to be trimmed`() {
        val name = "  FIFA  "
        val company = "  EA  "
        val genres = listOf("  Sports  ", "  Football  ", "  Online Competition  ")
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
            {
                "name": "$name",
                "developer": "$company",
                "genres": ["${genres[0]}", "${genres[1]}", "${genres[2]}"]
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(201, response.statusCode())

        val outputModel = Json.decodeFromString<GameOutputModel>(response.body())

        assertEquals(FIRST_GAME_ID + 1, outputModel.gId)
    }

    @Test
    fun `try to create game with invalid token`() {
        val name = "FIFA"
        val company = "EA"
        val genres = listOf("Sports", "Football")
        val invalidToken = "invalid token value" //just to force invalid token

        val requestBody = """
            {
                "name": "$name",
                "developer": "$company",
                "genres": ["${genres[0]}", "${genres[1]}"]
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $invalidToken")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(401, response.statusCode())
    }

    @Test
    fun `try to create game with invalid (empty) input`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
            {
                "name": "",
                "developer": "Rockstar Games",
                "genres": []
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(400, response.statusCode())
    }

    @Test
    fun `try to create game with repeated name`() {
        val name = "FIFA"
        val company = "EA"
        val genres = listOf("Sports", "Football")
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
            {
                "name": "$name",
                "developer": "$company",
                "genres": ["${genres[0]}", "${genres[1]}"]
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        //create game
        client.send(request, HttpResponse.BodyHandlers.ofString())
        //try to create game with same name
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(409, response.statusCode())
    }

    @Test
    fun `get game by id`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games/id/$FIRST_GAME_ID"))
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(200, response.statusCode())
    }

    @Test
    fun `try to get game by invalid id`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games/id/0"))
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(400, response.statusCode())
    }

    @Test
    fun `try to get non existing game by id`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games/id/${2 * FIRST_GAME_ID}"))
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(404, response.statusCode())
    }

    @Test
    fun `get game by name`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)
        val name = "GTA V"
        val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games/name/$encodedName"))
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(200, response.statusCode())
    }

    @Test
    fun `try to get non existing game by name`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)
        val name = "GTA VIII"
        val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games/name/$encodedName"))
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(404, response.statusCode())
    }

    @Test
    fun `get list of one game`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
            {
                "genres": ["Action"],
                "developer": "Rockstar Games"
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games/list"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(200, response.statusCode())
        assertEquals(1, Json.decodeFromString<GamesListOutputModel>(response.body()).games.size)
    }

    @Test
    fun `get list of many games with explicit limit and explicit skip`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        createMoreGames(foundPlayer.token)

        val requestBody = """
            {
                "genres": ["Action"],
                "developer": "Rockstar Games"
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games/list?skip=2&limit=4"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(200, response.statusCode())
        assertEquals(4, Json.decodeFromString<GamesListOutputModel>(response.body()).games.size)
    }

    @Test
    fun `get list of many games with default limit and default skip`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        createMoreGames(foundPlayer.token)

        val requestBody = """
            {
                "genres": ["Action"],
                "developer": "Rockstar Games"
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games/list"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(200, response.statusCode())
        assertEquals(LIMIT_DEFAULT, Json.decodeFromString<GamesListOutputModel>(response.body()).games.size)
    }

    @Test
    fun `try to get an empty list of games`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
            {
                "genres": ["Cars"],
                "developer": "Psyonix"
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games/list"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(200, response.statusCode())
        assertEquals(0, Json.decodeFromString<GamesListOutputModel>(response.body()).games.size)
    }

    @Test
    fun `try to get list of games with invalid input (no developer)`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
            {
                "genres": ["Action"],
                "developer": ""
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games/list"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(400, response.statusCode())
    }

    @Test
    fun `try to get list of games with invalid input (no genres)`() {
        val foundPlayer = playerServices.getPlayer(FIRST_PLAYER_ID)

        val requestBody = """
            {
                "developer": "Rockstar Games"
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/games/list"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${foundPlayer.token}")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(400, response.statusCode())
    }
}