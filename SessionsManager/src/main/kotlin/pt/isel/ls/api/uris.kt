package pt.isel.ls.api


const val API = "/api"

object GameUrisObj {
    const val CREATE = "$API/games"
    const val GET_BY_ID = "$API/games/id/{gid}"
    const val GET_BY_NAME = "$API/games/name/{gname}"
    const val GET_GAMES = "$API/games"
}

object Session {
    const val CREATE = "$API/sessions"
    const val GET = "$API/sessions/{sid}"
    const val ADD_PLAYER = "$API/sessions/{sid}/players"
    const val DELETE_SESSION = "$API/sessions/{sid}"
    const val DELETE_PLAYER = "$API/sessions/{sid}/players"
    const val UPDATE_SESSION = "$API/sessions/{sid}"
    const val GET_SESSIONS = "$API/sessions"
    const val   GET_GAMES_PLAYER_WILL_PARTICIPATE = "$API/sessions/player/{pid}"
}

object PlayerUris {
    const val CREATE = "$API/players"
    const val GET = "$API/players/{pid}"
    const val GET_BY_TOKEN = "$API/players/token/info"
}