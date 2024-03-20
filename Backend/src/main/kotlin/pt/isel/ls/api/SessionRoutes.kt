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

    private val logger = LoggerFactory.getLogger(SessionRoutes::class.java)

    val routes: RoutingHttpHandler =
        routes(
            Session.CREATE bind Method.POST to ::createSession,
            Session.GET bind Method.GET to ::getSession,
            Session.ADD_PLAYER bind Method.POST to ::addPlayerToSession,
            Session.GET_SESSIONS bind Method.GET to ::getListOfSessions
        )


    private fun createSession(request: Request): Response =
        exceptionAwareScope {
            val token = request.getAuthorizationToken()
            val inputModel = request.fromJson<CreateSessionInputModel>()
            val sid = services.createSession(token, inputModel.gid, inputModel.capacity, inputModel.date)
            Response(Status.CREATED).toJson(CreateSessionOutputModel(sid.toString()))
        }


    fun getSession(request: Request): Response =
        exceptionAwareScope {
            val sid = request.getSessionID()
            logger.debug("The session id in this request ${request.uri} is $sid")
            val session = services.getSession(sid)
            Response(Status.OK).toJson(SessionRetrievalOutputModel(session))
        }

    private fun addPlayerToSession(request: Request) : Response =
        exceptionAwareScope {
            val token = request.getAuthorizationToken()
            val sid = request.getSessionID()
            services.addPlayerToSession(token, sid)
            Response(Status.NO_CONTENT)// STATUS 204
        }

    private fun getListOfSessions(request: Request) : Response =
        exceptionAwareScope {
            val inputModel = request.fromJson<SessionListRetrievalInputModel>()
            val (skip, limit) = request.getSkipAndLimit()
            val sessions = services.getListOfSessions(inputModel.gid, inputModel.date, inputModel.state, inputModel.pid, skip, limit)
            Response(Status.OK).toJson(SessionListRetrievalOutputModel(sessions))
        }

}