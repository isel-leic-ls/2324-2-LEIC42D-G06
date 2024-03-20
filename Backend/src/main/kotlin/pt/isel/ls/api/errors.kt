package pt.isel.ls.api

import org.http4k.core.Status
import pt.isel.ls.repo.DomainException
import java.lang.IllegalArgumentException


val errors = mapOf(
    DomainException.InvalidToken::class to Status.UNAUTHORIZED,
    DomainException.BadRequestCreateGame::class to Status.BAD_REQUEST,
    DomainException.GameAlreadyExists::class to Status.CONFLICT,
    DomainException.GameNotFound::class to Status.NOT_FOUND,
    DomainException.PlayerNotFound::class to Status.NOT_FOUND,
    DomainException.SessionClosed::class to Status.BAD_REQUEST,
    DomainException.SessionNotFound::class to Status.NOT_FOUND,
    IllegalArgumentException::class to Status.BAD_REQUEST,
    DomainException.IllegalDate::class to Status.BAD_REQUEST
)