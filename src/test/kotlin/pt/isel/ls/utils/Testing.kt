package pt.isel.ls.utils

import org.postgresql.ds.PGSimpleDataSource

val dataSource = PGSimpleDataSource().apply {
    setUrl(System.getenv("JDBC_DATABASE_URL"))
}
