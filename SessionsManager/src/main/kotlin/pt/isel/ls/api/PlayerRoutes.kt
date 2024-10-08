package pt.isel.ls.api

import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import pt.isel.ls.services.PlayerServices
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.routes
import org.http4k.core.Status
import pt.isel.ls.api.model.*


class PlayerRoutes(private val services: PlayerServices) {
    val routes: RoutingHttpHandler = routes(
        PlayerUris.CREATE bind Method.POST to ::createPlayer,
        PlayerUris.GET bind Method.GET to ::getPlayerDetails,
        PlayerUris.GET_BY_TOKEN bind Method.GET to ::getPlayerIdByToken,
        PlayerUris.GET_BY_USERNAME bind Method.GET to ::getPlayersByUsername,
        PlayerUris.LOGIN bind Method.POST to ::login
    )

    private fun login(request: Request) =
        exceptionAwareScope {
            val inputModel = request.fromJson<LoginInputModel>()
            val (pid, token) = services.login(inputModel.name, inputModel.password)
            Response(Status.OK).toJson(PlayerOutputModel(pid, token))
        }

    private fun createPlayer(request: Request) =
        exceptionAwareScope {
            val inputModel = request.fromJson<PlayerInputModel>()
            val (token, pid) = services.createPlayer(inputModel.name, inputModel.email, inputModel.password)
            Response(Status.CREATED).toJson(PlayerOutputModel(pid, token))
        }

    private fun getPlayerDetails(request: Request) =
        exceptionAwareScope {
            val pid = request.getPlayerDetails()
            val player = services.getPlayer(pid)
            Response(Status.OK).toJson(PlayerRetrievalOutputModel(player.id, player.name, player.email))
        }

    private fun getPlayerIdByToken(request: Request) =
        exceptionAwareScope {
            val token = request.getAuthorizationToken()
            val pid = services.getPlayerIdByToken(token)
            Response(Status.OK).toJson(PlayerOutputModel(pid, token))
        }

    private fun getPlayersByUsername(request: Request) =
        exceptionAwareScope {
            val username = request.getUsername()
            val (skip, limit) = request.getSkipAndLimit()
            val (list, total) = services.getPlayersByUsername(username, skip, limit)
            Response(Status.OK).toJson(PlayersListRetrievalOutputModel(list, total))
        }
}