package pt.isel.ls.utils

import pt.isel.ls.domain.GameDetails
import java.util.*
import kotlin.random.Random


const val ALPHABET = "abcdefghijklmnopqrstuvwxyz"
val DOMAINS = listOf("@gmail.com", "@hotmail.com","@outlook.com")
val GENRES = listOf("Action", "Adventure", "RPG")
const val NAME_LENGTH = 10

data class PlayerDetails(val name : String, val email : String, val token : String)

fun generatePlayerDetails() : PlayerDetails {
    val name = generateName()
    val email = "$name${DOMAINS.random()}"
    return PlayerDetails(name, email, UUID.randomUUID().toString())
}

fun generateGameDetails() : GameDetails {
    val name = generateName()
    val developer = generateName()
    val genresCount = Random.nextInt(1,3)
    val genresSet = mutableSetOf<String>()

    repeat(genresCount) {
        genresSet.add(GENRES.random())
    }
    return GameDetails(name, developer, genresSet.toList())
}

fun generateName() : String =
    (NAME_LENGTH /2..NAME_LENGTH).map { ALPHABET.random() }.joinToString("")