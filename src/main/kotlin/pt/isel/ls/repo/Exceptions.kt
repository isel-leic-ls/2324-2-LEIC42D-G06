package pt.isel.ls.repo


object Exceptions : Exception() {
    object SessionNotFound : Exception()
    class GameNotFound(message: String) : Exception(message)
}