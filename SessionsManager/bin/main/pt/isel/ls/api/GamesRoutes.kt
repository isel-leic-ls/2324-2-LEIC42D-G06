package pt.isel.ls.api

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.slf4j.LoggerFactory
import pt.isel.ls.api.model.GameInputModel
import pt.isel.ls.api.model.GameOutputModel
import pt.isel.ls.api.model.GamesListInputModel
import pt.isel.ls.api.model.GamesListOutputModel
import pt.isel.ls.services.GamesServices
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


class GamesRoutes(private val services: GamesServices) {
    val routes: RoutingHttpHandler =
        routes(
            GameUrisObj.CREATE bind Method.POST to ::createGame,
            GameUrisObj.GET_BY_ID bind Method.GET to ::getGameDetailsById,
            GameUrisObj.GET_BY_NAME bind Method.GET to ::getGameDetailsByName,
            GameUrisObj.GET_GAMES bind Method.POST to ::getListOfGames
        )

    private fun createGame(request: Request): Response =
        exceptionAwareScope {
            val token = request.getAuthorizationToken()
            val inputModel = request.fromJson<GameInputModel>()
            //trim the input
            val trimmedName = inputModel.name.trim()
            val trimmedDev = inputModel.developer.trim()
            val trimmedGenres = inputModel.genres.map { it.trim() }
            //create the game
            val gId = services.createGame(token, trimmedName, trimmedDev, trimmedGenres)
            Response(Status.CREATED).toJson(GameOutputModel(gId))
        }

    private fun getGameDetailsById(request: Request): Response =
        exceptionAwareScope {
            val gId = request.getGameId()
            val game = services.getDetailsOfGameById(gId)
            Response(Status.OK).toJson(game)
        }

    private fun getGameDetailsByName(request: Request): Response =
        exceptionAwareScope {
            val encodedName = request.getGameName()
            val decodedName = URLDecoder.decode(encodedName, StandardCharsets.UTF_8.toString())
            val game = services.getDetailsOfGameByName(decodedName)
            Response(Status.OK).toJson(game)
        }


    private fun getListOfGames(request: Request): Response =
        exceptionAwareScope {
            val inputModel = request.fromJson<GamesListInputModel>()
            val (skip, limit) = request.getSkipAndLimit()
            val games = services.getListOfGames(inputModel.genres, inputModel.developer, limit, skip)
            Response(Status.OK).toJson(GamesListOutputModel(games))
        }
}