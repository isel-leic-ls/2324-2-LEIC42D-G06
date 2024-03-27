package pt.isel.ls.api

import org.http4k.core.Status
import pt.isel.ls.AppException
import java.lang.IllegalArgumentException

// Converts the app exceptions to the corresponding HTTP status code.
val errors = mapOf(
    AppException.InvalidToken::class to Status.UNAUTHORIZED,
    AppException.InvalidAuthorization::class to Status.UNAUTHORIZED,
    AppException.BadRequestCreateGame::class to Status.BAD_REQUEST,
    AppException.GameAlreadyExists::class to Status.CONFLICT,
    AppException.GameNotFound::class to Status.NOT_FOUND,
    AppException.PlayerNotFound::class to Status.NOT_FOUND,
    AppException.PlayerAlreadyExists::class to Status.CONFLICT,
    AppException.PlayerAlreadyInSession::class to Status.BAD_REQUEST,
    AppException.SessionClosed::class to Status.BAD_REQUEST,
    AppException.SessionNotFound::class to Status.NOT_FOUND,
    AppException.SQLException::class to Status.INTERNAL_SERVER_ERROR,
    AppException.PlayerNotFoundInSession::class to Status.BAD_REQUEST,
    AppException.PlayerCantDeleteSession::class to Status.BAD_REQUEST,
    IllegalArgumentException::class to Status.BAD_REQUEST,
)