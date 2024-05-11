package pt.isel.ls.api


const val API = "/api"

object GameUrisObj {
    const val CREATE = "$API/games"
    const val GET_BY_ID = "$API/games/id/{gid}"
    const val GET_BY_NAME = "$API/games/name/{gname}"
    const val GET_GAMES_BY_NAME = "$API/games/list"
    const val GET_GAMES_BY_GENRES_DEV = "$API/games"
}

object Session {
    const val CREATE = "$API/sessions"
    const val GET = "$API/sessions/{sid}"
    const val ADD_PLAYER = "$API/sessions/{sid}/players"
    const val DELETE_SESSION = "$API/sessions/{sid}"
    const val DELETE_PLAYER = "$API/sessions/{sid}/players"
    const val UPDATE_SESSION = "$API/sessions/{sid}"
    const val GET_SESSIONS = "$API/sessions"
}

object PlayerUris {
    const val CREATE = "$API/players"
    const val LOGIN = "$API/players/login"
    const val GET = "$API/players/{pid}"
    const val GET_BY_TOKEN = "$API/players/token/info"
    const val GET_BY_USERNAME = "$API/players"
}