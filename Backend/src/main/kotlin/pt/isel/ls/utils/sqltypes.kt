package pt.isel.ls.utils

import java.sql.Timestamp
import java.time.LocalDateTime

fun LocalDateTime.toSqlTimestamp() = Timestamp.valueOf(this)