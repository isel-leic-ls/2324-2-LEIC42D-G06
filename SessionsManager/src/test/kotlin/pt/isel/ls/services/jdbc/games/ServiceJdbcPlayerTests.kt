package pt.isel.ls.services.jdbc.games

import org.junit.Test
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.repo.jdbc.JdbcPlayersRepo
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.utils.Environment
import kotlin.test.assertFailsWith

class ServiceJdbcPlayerTests {

    private val dataSource = PGSimpleDataSource().apply {
        setUrl(Environment.DATABASE_TEST_URL)
    }

    private val pRepo = JdbcPlayersRepo(dataSource)

    @Test
    fun retrievingTheGamesOfAPlayerWithInvalidSkip() {
        val service = PlayerServices(pRepo)

        assertFailsWith<IllegalArgumentException> {
            service.getPlayerPlayedGames(1, -1, 10)
        }

    }

    @Test
    fun retrievingTheGamesOfAPlayerWithInvalidLimit() {
        val service = PlayerServices(pRepo)

        assertFailsWith<IllegalArgumentException> {
            service.getPlayerPlayedGames(1, 0, 0)
        }

    }
}