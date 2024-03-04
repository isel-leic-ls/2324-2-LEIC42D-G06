package pt.isel.ls.repo


class Exceptions : Exception() {
    class SessionNotFound : Exception()
    class GameNotFound(message: String) : Exception(message)
}