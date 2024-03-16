package pt.isel.ls.api

object Session {
    const val CREATE = "/sessions"
    const val GET = "/sessions/{sid}"
    const val ADD_PLAYER = "/sessions/{sid}/players"
    const val GET_SESSIONS = "/sessions"
}