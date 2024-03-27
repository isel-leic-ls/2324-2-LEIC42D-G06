package pt.isel.ls.api

import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.slf4j.LoggerFactory
import pt.isel.ls.services.PlayerServices
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.routes
import pt.isel.ls.api.model.PlayerInputModel
import org.http4k.core.Status
import pt.isel.ls.api.model.PlayerOutputModel
import pt.isel.ls.api.model.PlayerRetrievalOutputModel

class PlayerRoutes(private val services: PlayerServices) {

    val routes: RoutingHttpHandler = routes(
        PlayerUris.CREATE bind Method.POST to ::createPlayer,
        PlayerUris.GET bind Method.GET to ::getPlayerDetails,
    )

    private fun createPlayer(request: Request) =
        exceptionAwareScope {
            val inputModel = request.fromJson<PlayerInputModel>()
            val (token, pid) = services.createPlayer(inputModel.name, inputModel.email, inputModel.password)
            Response(Status.CREATED).toJson(PlayerOutputModel(pid, token))
        }


    private fun getPlayerDetails(request: Request) = exceptionAwareScope {
        val pid = request.getPlayerDetails()
        val player = services.getPlayer(pid)
        Response(Status.OK).toJson(PlayerRetrievalOutputModel(player.id, player.name, player.email))
    }

}
