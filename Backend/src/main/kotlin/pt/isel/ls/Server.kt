package pt.isel.ls

import org.http4k.routing.routes
import org.slf4j.LoggerFactory
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.api.GamesRoutes
import pt.isel.ls.api.PlayerRoutes
import pt.isel.ls.api.SessionRoutes
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.repo.mem.MemSessionRepo
import pt.isel.ls.services.Services
import pt.isel.ls.utils.FIRST_PLAYER_ID
import java.util.*


fun main() {
    val logger = LoggerFactory.getLogger("pt.isel.ls.Server")
    val port = 8080
    // initialize the repositories
    // todo: the repositories are supposed to be the JDBC ones, for that we get the connection string from the ENV variables
    // todo: and create the datasource using the connection string
    // todo: and then pass the datasource to the repositories
    val playersRepo = MemPlayersRepo()
    val gamesRepo = MemGamesRepo()
    val servicesRepo = MemSessionRepo()

    // insert dummy data
    // todo: remove this when using the real repositories

    // create players
    playersRepo.createPlayer("joao", "joao@hotmail.com", UUID.randomUUID().toString(),"joao123")
    playersRepo.createPlayer("pedro", "pedro@gmail.com", UUID.randomUUID().toString(),"pedro123")
    playersRepo.createPlayer("Vasco", "vasco@gmail.com", UUID.randomUUID().toString(),"vasco123")
    println(playersRepo.getPlayer(FIRST_PLAYER_ID))
    println(playersRepo.getPlayer(FIRST_PLAYER_ID + 1))

    // create some games
    gamesRepo.insert("FIFA", "EA", listOf("Sports", "Football"))

    // create the services
    val services = Services(gamesRepo, playersRepo, servicesRepo)
    val sessionRoutes = SessionRoutes(services.sessionService) // server only using the session routes for now...
    val gamesRoutes = GamesRoutes(services.gameService)
    val playersRoutes = PlayerRoutes(services.playerService)

    // combine the routes
    val routes = routes(
        sessionRoutes.routes,
        gamesRoutes.routes,
        playersRoutes.routes
    )

    // start the server with the routes
    val server = routes.asServer(Jetty(port)).start()

    logger.info("Server started on port $port")
    println("Press enter to stop the server")
    readln()

    server.stop()
}