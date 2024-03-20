package pt.isel.ls.api

import org.http4k.core.Status
import pt.isel.ls.AppException
import java.lang.IllegalArgumentException


val errors = mapOf(
    AppException.InvalidToken::class to Status.UNAUTHORIZED,
    AppException.BadRequestCreateGame::class to Status.BAD_REQUEST,
    AppException.GameAlreadyExists::class to Status.CONFLICT,
    AppException.GameNotFound::class to Status.NOT_FOUND,
    AppException.PlayerNotFound::class to Status.NOT_FOUND,
    AppException.SessionClosed::class to Status.BAD_REQUEST,
    AppException.SessionNotFound::class to Status.NOT_FOUND,
    IllegalArgumentException::class to Status.BAD_REQUEST,
)