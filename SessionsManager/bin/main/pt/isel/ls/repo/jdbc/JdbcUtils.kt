package pt.isel.ls.repo.jdbc

import java.sql.PreparedStatement

fun PreparedStatement.bindParameters(
    vararg args : Any?,
) : PreparedStatement =
    apply {
        args.filter { it != null }
            .forEachIndexed { index, any -> setObject(index + 1, any) }
    }

