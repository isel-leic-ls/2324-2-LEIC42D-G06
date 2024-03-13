package pt.isel.ls.utils

import pt.isel.ls.repo.DomainException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


const val INITIAL_GAME_ID = 100 //gameId starts at 100 and goes up 1 by 1
const val SKIP_DEFAULT = 0
const val LIMIT_DEFAULT = 10
const val CAPACITY_LOWER_BOUND = 2
const val CAPACITY_UPPER_BOUND = 10
const val DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
val DATE_REGEX = Regex("^([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})\$")
const val TOKEN_LENGTH = 36

val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

fun String.isDateWellFormatted() =
    DATE_REGEX.matches(this)

fun String.toDate() : LocalDateTime =
    LocalDateTime.parse(this, dateFormatter)