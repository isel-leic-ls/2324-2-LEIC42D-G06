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
import pt.isel.ls.utils.FIRST_PLAYER_ID

private const val TOKEN = "3ad7db4b-c5a9-42ee-9094-852f94c57cb7"

class PlayerRoutesMock() {

    val routes: RoutingHttpHandler = routes(
        PlayerUris.CREATE bind Method.POST to ::createPlayer,
        PlayerUris.GET bind Method.GET to ::getPlayerDetails
    )

    private fun createPlayer(request: Request) =
        exceptionAwareScope {
            request.fromJson<PlayerInputModel>()
            val (token, pid) = TOKEN to FIRST_PLAYER_ID
            Response(Status.CREATED).toJson(PlayerOutputModel(pid, token))
        }


    private fun getPlayerDetails(request: Request) = exceptionAwareScope {
        val pid = request.getPlayerDetails()
        val player = if(pid == FIRST_PLAYER_ID) Player(FIRST_PLAYER_ID,"teste","teste@gmail.com",TOKEN,"teste")
        else throw AppException.PlayerNotFound("No player found with the given Id")
        Response(Status.OK).toJson(PlayerRetrievalOutputModel(player.id, player.name, player.email))
    }


}