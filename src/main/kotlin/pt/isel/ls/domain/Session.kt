package pt.isel.ls.domain

import java.util.Date

data class Session(val id: Int, val capacity: Int, val date: Date, val game: Game, val players: Array<Player>)
