package pt.isel.ls.api


object GameUrisObj {
    const val CREATE = "/games"
    const val GET_BY_ID = "/games/id/{gid}"
    const val GET_BY_NAME = "/games/name/{name}"
    const val GET_GAMES = "/games&limit={limit}&skip={skip}"
}

object Session {
    const val CREATE = "/sessions"
    const val GET = "/sessions/{sid}"
    const val ADD_PLAYER = "/sessions/{sid}/players"
    const val GET_SESSIONS = "/sessions"
}