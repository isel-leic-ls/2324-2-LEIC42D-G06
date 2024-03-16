package pt.isel.ls.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"
val DATE_REGEX = Regex("^([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})\$")

val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

fun String.isDateWellFormatted() =
    DATE_REGEX.matches(this)

fun String.toDate() : LocalDateTime =
    LocalDateTime.parse(this, dateFormatter)