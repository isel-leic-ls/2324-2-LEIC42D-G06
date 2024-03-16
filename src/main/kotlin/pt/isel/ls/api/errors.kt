package pt.isel.ls.api

import org.http4k.core.Status
import pt.isel.ls.repo.DomainException
import java.lang.IllegalArgumentException

val errors = mapOf(
    DomainException.PlayerNotFound::class to Status.NOT_FOUND,
    DomainException.SessionClosed::class to Status.BAD_REQUEST,
    DomainException.GameNotFound::class to Status.NOT_FOUND,
    DomainException.SessionNotFound::class to Status.NOT_FOUND,
    IllegalArgumentException::class to Status.BAD_REQUEST
)