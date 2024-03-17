package pt.isel.ls

import org.slf4j.LoggerFactory
import org.http4k.server.Jetty
import org.http4k.server.asServer
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
    val playersRepo = MemPlayersRepo()
    val gamesRepo = MemGamesRepo()
    val servicesRepo = MemSessionRepo()

    // create some players
    playersRepo.createPlayer("joao", "joao@hotmail.com", UUID.randomUUID().toString())
    playersRepo.createPlayer("pedro", "pedro@gmail.com", UUID.randomUUID().toString())
    println(playersRepo.getPlayer(FIRST_PLAYER_ID))
    println(playersRepo.getPlayer(FIRST_PLAYER_ID + 1))

    // create some games
    gamesRepo.insert("FIFA", "EA", listOf("Sports", "Football"))

    // create the services
    val services = Services(gamesRepo, playersRepo, servicesRepo)
    val serviceRoutes = SessionRoutes(services.sessionService) // server only using the session routes for now...

    // start the server
    val server = serviceRoutes.routes.asServer(Jetty(port)).start()

    logger.info("Server started on port $port")
    println("Press enter to stop the server")
    readln()

    server.stop()
}