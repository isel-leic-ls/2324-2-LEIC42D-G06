package pt.isel.ls.utils

object Environment {
    val DATABASE_URL = System.getenv("JDBC_DATABASE_URL")
    val DATABASE_TEST_URL = System.getenv("JDBC_DATABASE_TEST_URL")
}