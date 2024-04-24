package pt.isel.ls.api

import org.http4k.core.Request
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.slf4j.LoggerFactory
import pt.isel.ls.api.model.*
import pt.isel.ls.services.SessionServices

class SessionRoutes(private val services : SessionServices) {

    val routes: RoutingHttpHandler =
        routes(
            Session.CREATE bind Method.POST to ::createSession,
            Session.GET bind Method.GET to ::getSession,
            Session.ADD_PLAYER bind Method.PUT to ::addPlayerToSession,
            Session.GET_SESSIONS bind Method.GET to ::getListOfSessions,
            Session.DELETE_SESSION bind Method.DELETE to ::deleteSession,
            Session.DELETE_PLAYER bind Method.DELETE to ::deletePlayerFromSession,
            Session.UPDATE_SESSION bind Method.PUT to ::updateSession,
            Session.GET_GAMES_PLAYER_WILL_PARTICIPATE bind Method.GET to ::getGamesPlayerWillParticipate
        )


    private fun createSession(request: Request): Response =
        exceptionAwareScope {
            val token = request.getAuthorizationToken()
            val inputModel = request.fromJson<CreateSessionInputModel>()
            val sid = services.createSession(token, inputModel.gid, inputModel.capacity, inputModel.date)
            Response(Status.CREATED).toJson(CreateSessionOutputModel(sid.toString()))
        }


    private fun getSession(request: Request): Response =
        exceptionAwareScope {
            val sid = request.getSessionID()
            val session = services.getSession(sid)
            Response(Status.OK).toJson(SessionRetrievalOutputModel(session))
        }

    private fun addPlayerToSession(request: Request) : Response =
        exceptionAwareScope {
            val token = request.getAuthorizationToken()
            val sid = request.getSessionID()
            services.addPlayerToSession(token, sid)
            Response(Status.NO_CONTENT)
        }

    private fun deleteSession(request: Request) : Response =
        exceptionAwareScope {
            val token = request.getAuthorizationToken()
            val sid = request.getSessionID()
            services.deleteSession(token, sid)
            Response(Status.NO_CONTENT)
        }

    private fun updateSession(request: Request) : Response =
        exceptionAwareScope {
            val token = request.getAuthorizationToken()
            val sid = request.getSessionID()
            val (capacity, date) = request.fromJson<SessionUpdateInputModel>()
            services.updateSession(token, sid, date, capacity)
            Response(Status.NO_CONTENT)
        }

    private fun deletePlayerFromSession(request: Request) : Response =
        exceptionAwareScope {
            val token = request.getAuthorizationToken()
            val sid = request.getSessionID()
            services.deletePlayerFromSession(token, sid)
            Response(Status.NO_CONTENT)
        }

    private fun getListOfSessions(request: Request): Response =
        exceptionAwareScope {
            val (gid, date, state, pid) = request.getSessionsListInputModel()
            val (skip, limit) = request.getSkipAndLimit()
            val (sessions, total) = services.getListOfSessions(gid, date, state, pid, skip, limit)
            Response(Status.OK).toJson(SessionListRetrievalOutputModel(sessions, total))
        }

    private fun getGamesPlayerWillParticipate(request: Request): Response =
        exceptionAwareScope {
            val pid = request.getPlayerDetails()
            val (skip, limit) = request.getSkipAndLimit()
            val (games, total) = services.getListOfGamesThatPlayerWillParticipate(pid, skip, limit)
            Response(Status.OK).toJson(GamesListOutputModel(games, total))
        }
}
