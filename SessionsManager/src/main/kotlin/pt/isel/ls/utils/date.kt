package pt.isel.ls.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


const val DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"
val DATE_TIME_REGEX = Regex("^([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})\$")
val DATE_REGEX = Regex("^([0-9]{4})-([0-9]{2})-([0-9]{2})\$")
val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)

fun String.isDateTimeWellFormatted() = DATE_TIME_REGEX.matches(this)

fun String.isDateWellFormatted() = DATE_REGEX.matches(this)

fun String.checkIfDateIsAfterNow() = this.toDate().isAfter(LocalDateTime.now())

fun String.toDate(): LocalDateTime = LocalDateTime.parse(this, DATE_TIME_FORMATTER)