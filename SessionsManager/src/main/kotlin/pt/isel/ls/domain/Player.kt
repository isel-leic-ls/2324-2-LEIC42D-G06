package pt.isel.ls.domain

import kotlinx.serialization.Serializable
import pt.isel.ls.utils.MAX_NAME_LENGTH
import pt.isel.ls.utils.MAX_PASSWORD_LENGTH
import pt.isel.ls.utils.MIN_NAME_LENGTH
import pt.isel.ls.utils.MIN_PASSWORD_LENGTH

val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
val PASSWORD_RANGE = MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH
val NAME_RANGE = MIN_NAME_LENGTH..MAX_NAME_LENGTH

@Serializable
data class Player(val id: Int, val name: String, val email: String, val token : String, val password: String)

@Serializable
data class PlayerDetails(val id: Int, val name: String, val email: String)

fun validatePlayerCredentials(name: String, email: String, password: String) {
    require(name.isNotBlank()) { "Name cannot be blank" }
    require(name.length in NAME_RANGE) { "Name length must be between $MIN_NAME_LENGTH and $MAX_NAME_LENGTH" }
    require(password.length in PASSWORD_RANGE) { "Password length must be between $MIN_PASSWORD_LENGTH and $MAX_PASSWORD_LENGTH" }
    require(password.isNotBlank()) { "Password cannot be blank" }
    require(email.matches(EMAIL_REGEX)) { "Invalid email" }
}