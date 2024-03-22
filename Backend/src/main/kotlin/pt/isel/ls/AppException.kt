package pt.isel.ls


/** Represents the custom exceptions that can be thrown by the application. */
sealed class AppException : Exception() {
    class InvalidToken(message: String) : Exception(message)

    class BadRequestCreateGame(message: String) : Exception(message)

    class GameAlreadyExists(message: String) : Exception(message)

    class GameNotFound(message: String) : Exception(message)

    class SessionNotFound(message: String) : Exception(message)

    class SessionClosed(message: String) : Exception(message)

    class PlayerAlreadyInSession(message: String) : Exception(message)

    class PlayerNotFound(message: String) : Exception(message)

    class SQLException(message: String) : Exception(message)
}