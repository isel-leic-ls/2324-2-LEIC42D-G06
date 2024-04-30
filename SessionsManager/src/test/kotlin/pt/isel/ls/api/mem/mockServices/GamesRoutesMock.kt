package pt.isel.ls.api.mem.mockServices

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.api.*
import pt.isel.ls.api.model.GameInputModel
import pt.isel.ls.api.model.GameOutputModel
import pt.isel.ls.api.model.GamesListOutputModel
import pt.isel.ls.domain.Game
import pt.isel.ls.utils.FIRST_GAME_ID


class GamesRoutesMock {
    val routes: RoutingHttpHandler = routes(
        GameUrisObj.CREATE bind Method.POST to ::createGame,
        GameUrisObj.GET_BY_ID bind Method.GET to ::getGameDetailsById,
        GameUrisObj.GET_BY_NAME bind Method.GET to ::getGameDetailsByName,
        GameUrisObj.GET_GAMES_BY_NAME bind Method.GET to ::getGamesByName,
        GameUrisObj.GET_GAMES_BY_GENRES_DEV bind Method.GET to ::getGamesByGenresDev,
    )

    private fun createGame(request: Request): Response =
        exceptionAwareScope {
            request.fromJson<GameInputModel>()
            Response(Status.CREATED).toJson(GameOutputModel(FIRST_GAME_ID + 1))
        }

    private fun getGameDetailsById(request: Request): Response =
        exceptionAwareScope {
            val gId = request.getGameId()
            val game = Game(gId, "gName", "gDev", listOf("gGenre"))
            Response(Status.OK).toJson(game)
        }

    private fun getGameDetailsByName(request: Request): Response =
        exceptionAwareScope {
            val gName = request.getGameName()
            val game = Game(FIRST_GAME_ID, gName, "gDev", listOf("gGenre"))
            Response(Status.OK).toJson(game)
        }

    private fun getGamesByName(request: Request): Response =
        exceptionAwareScope {
            val partialName = request.getPartialGameName()
            val (games, total) = Pair(
                listOf(
                    Game(FIRST_GAME_ID, partialName + "A IV", "gDev", listOf("gGenre")),
                    Game(FIRST_GAME_ID + 1, partialName + "A V", "gDev", listOf("gGenre"))
                ), -1
            )
            Response(Status.OK).toJson(GamesListOutputModel(games, total))
        }

    private fun getGamesByGenresDev(request: Request): Response =
        exceptionAwareScope {
            val (genres, dev) = request.getGamesListInputModel()
            val (games, total) = Pair(listOf(Game(FIRST_GAME_ID, "gName", dev, genres)), -1)
            Response(Status.OK).toJson(GamesListOutputModel(games, total))
        }
}