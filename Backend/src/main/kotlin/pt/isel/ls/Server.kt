package pt.isel.ls

import org.http4k.routing.routes
import org.slf4j.LoggerFactory
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.api.GamesRoutes
import pt.isel.ls.api.PlayerRoutes
import pt.isel.ls.api.SessionRoutes
import pt.isel.ls.repo.jdbc.JdbcGamesRepo
import pt.isel.ls.repo.jdbc.JdbcPlayersRepo
import pt.isel.ls.repo.jdbc.JdbcSessionsRepo
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.repo.mem.MemSessionRepo
import pt.isel.ls.services.Services
import pt.isel.ls.utils.FIRST_PLAYER_ID
import java.util.*


fun main() {
    val logger = LoggerFactory.getLogger("pt.isel.ls.Server")

    // set the port and datasource
    val port = 8080

    val dataSource = PGSimpleDataSource().apply {
        setUrl(System.getenv("JDBC_DATABASE_URL"))
    }

    // initialize the repositories
    val gamesRepo = JdbcGamesRepo(dataSource)
    val playersRepo = JdbcPlayersRepo(dataSource)
    val sessionsRepo = JdbcSessionsRepo(dataSource)

    // create the services
    val services = Services(gamesRepo, playersRepo, sessionsRepo)
    val sessionRoutes = SessionRoutes(services.sessionService)
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