package pt.isel.ls

import org.postgresql.ds.PGSimpleDataSource

fun main(){

    val dataSource = PGSimpleDataSource().apply {
        setUrl(System.getenv("JDBC_DATABASE_URL"))
    }

    dataSource.getConnection().use {
        val stm = it.prepareStatement("select * from students")
        val rs = stm.executeQuery()
        while (rs.next()) {
            println(rs.getInt("id"))
            println(rs.getString("name"))
            println(rs.getString("email"))
        }
    }
}