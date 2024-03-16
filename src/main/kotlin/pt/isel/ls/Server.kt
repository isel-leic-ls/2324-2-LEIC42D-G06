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
import java.util.*

fun main() {
    val port = 8080

    val playersRepo = MemPlayersRepo()
    playersRepo.createPlayer("joao", "joao@hotmail.com", UUID.randomUUID().toString())
    playersRepo.createPlayer("pedro", "pedro@gmail.com", UUID.randomUUID().toString())
    println(playersRepo.getPlayer(1))
    println(playersRepo.getPlayer(2))

    val gamesRepo = MemGamesRepo()
    gamesRepo.insert("FIFA", "EA", listOf("Sports", "Football"))

    val sessionRepo = MemSessionRepo()
    val sessionService = SessionServices(playersRepo, gamesRepo, sessionRepo)
    val serviceRoutes = SessionRoutes(sessionService)

    val server = serviceRoutes.routes.asServer(Jetty(port)).start()

    readln()

    server.stop()
}