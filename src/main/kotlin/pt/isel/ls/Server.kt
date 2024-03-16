package pt.isel.ls

import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.api.SessionRoutes
import pt.isel.ls.repo.interfaces.PlayersRepo
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.repo.mem.MemSessionRepo
import pt.isel.ls.services.GamesServices
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.services.SessionServices

fun main() {
    val port = 8080
    val playersRepo = MemPlayersRepo()
    val gamesRepo = MemGamesRepo()
    val sessionRepo = MemSessionRepo()
    //val playerService = PlayerServices(playersRepo)
    //val gamesServices = GamesServices(gamesRepo)
    val sessionService = SessionServices(playersRepo, gamesRepo, sessionRepo)

    val serviceRoutes = SessionRoutes(sessionService)

    val server = serviceRoutes.routes.asServer(Jetty(port)).start()

    readln()

    server.stop()
}