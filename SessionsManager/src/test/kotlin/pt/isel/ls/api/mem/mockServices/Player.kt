package pt.isel.ls.api.mem.mockServices

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import pt.isel.ls.AppException
import pt.isel.ls.api.*
import pt.isel.ls.api.model.PlayerInputModel
import pt.isel.ls.api.model.PlayerOutputModel
import pt.isel.ls.api.model.PlayerRetrievalOutputModel
import pt.isel.ls.domain.Player

class PlayerRoutesMock() {

    val routes: RoutingHttpHandler = routes(
        PlayerUris.CREATE bind Method.POST to ::createPlayer,
        PlayerUris.GET bind Method.GET to ::getPlayerDetails
    )

    private fun createPlayer(request: Request) =
        exceptionAwareScope {
            request.fromJson<PlayerInputModel>()
            val (token, pid) = "3ad7db4b-c5a9-42ee-9094-852f94c57cb7" to 1000
            Response(Status.CREATED).toJson(PlayerOutputModel(pid, token))
        }


    private fun getPlayerDetails(request: Request) = exceptionAwareScope {
        val pid = request.getPlayerDetails()
        val player = if(pid == 1000) Player(1000,"teste","teste@gmail.com","3ad7db4b-c5a9-42ee-9094-852f94c57cb7","teste")
        else throw AppException.PlayerNotFound("No player found with the given Id")
        Response(Status.OK).toJson(PlayerRetrievalOutputModel(player.id, player.name, player.email))
    }


}