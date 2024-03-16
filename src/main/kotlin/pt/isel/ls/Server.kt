package pt.isel.ls

import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.api.SessionRoutes
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.repo.mem.MemSessionRepo
import pt.isel.ls.services.Services
import java.util.*

fun main() {
    val port = 8080

    // initialize the repositories
    val playersRepo = MemPlayersRepo()
    val gamesRepo = MemGamesRepo()
    val servicesRepo = MemSessionRepo()

    // create some players
    playersRepo.createPlayer("joao", "joao@hotmail.com", UUID.randomUUID().toString())
    playersRepo.createPlayer("pedro", "pedro@gmail.com", UUID.randomUUID().toString())
    println(playersRepo.getPlayer(1))
    println(playersRepo.getPlayer(2))

    // create some games
    gamesRepo.insert("FIFA", "EA", listOf("Sports", "Football"))

    // create the services
    val services = Services(gamesRepo, playersRepo, servicesRepo)
    val serviceRoutes = SessionRoutes(services.sessionService) // server only using the session routes for now...

    // start the server
    val server = serviceRoutes.routes.asServer(Jetty(port)).start()

    readln()

    server.stop()
}