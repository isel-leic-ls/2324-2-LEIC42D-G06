package pt.isel.ls.utils

import pt.isel.ls.services.session.*
import kotlin.random.Random

const val ALPHABET = "abcdefghijklmnopqrstuvwxyz"
val DOMAINS = listOf("@gmail.com", "@hotmail.com","@outlook.com")
val GENRES = listOf("Action", "Adventure", "RPG")
const val NAME_LENGTH = 10

fun generateNameAndEmail() : Pair<String,String> {
    val name = generateName()
    val email = "$name${DOMAINS.random()}"
    return name to email
}

fun generateGameInfo() : GameInfo {
    val name = generateName()
    val developer = generateName()
    val genresCount = Random.nextInt(1,3)
    val genresSet = mutableSetOf<String>()

    repeat(genresCount) {
        genresSet.add(GENRES.random())
    }
    return GameInfo(name, developer, genresSet.toList())
}

fun generateName() : String =
    (NAME_LENGTH /2..NAME_LENGTH).map { ALPHABET.random() }.joinToString("")