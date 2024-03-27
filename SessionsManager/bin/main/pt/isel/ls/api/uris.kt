package pt.isel.ls.api


object GameUrisObj {
    const val CREATE = "/games"
    const val GET_BY_ID = "/games/id/{gid}"
    const val GET_BY_NAME = "/games/name/{gname}"
    const val GET_GAMES = "/games/list"
}

object Session {
    const val CREATE = "/sessions"
    const val GET = "/sessions/{sid}"
    const val ADD_PLAYER = "/sessions/{sid}/players"
    const val DELETE_SESSION = "/sessions/{sid}"
    const val DELETE_PLAYER = "/sessions/{sid}/players"
    const val UPDATE_SESSION = "/sessions/{sid}"
    const val GET_SESSIONS = "/sessions/list"
}

object PlayerUris {
    const val CREATE = "/players"
    const val GET = "/players/{pid}"
}