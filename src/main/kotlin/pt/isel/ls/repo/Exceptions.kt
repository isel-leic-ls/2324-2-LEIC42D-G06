package pt.isel.ls.repo


// This could be a sealed class..
object Exceptions : Exception() {
    object SessionNotFound : Exception()
    class GameNotFound(message: String) : Exception(message)
    class PlayerNotFound(message : String) : Exception(message)

    class IllegalDate(message : String) : Exception(message)
}