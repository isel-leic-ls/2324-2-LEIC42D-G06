package pt.isel.ls.domain

import java.util.UUID

data class Player(val id: Int, val name: String, val email: String, val token : String) {
}