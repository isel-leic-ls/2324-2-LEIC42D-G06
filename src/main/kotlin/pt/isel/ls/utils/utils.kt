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
const val TOKEN_LENGTH = 36

val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

fun String.toDate() : LocalDateTime {
    val dateTime = LocalDateTime.parse(this, dateFormatter)
    val currentTime = LocalDateTime.now()
    if(dateTime.isBefore(currentTime))
        throw DomainException.IllegalDate("Date must be in the future")

    return dateTime
}