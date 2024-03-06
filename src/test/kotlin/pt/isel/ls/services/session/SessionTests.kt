package pt.isel.ls.services.session

import pt.isel.ls.domain.State
import pt.isel.ls.repo.DomainException
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.repo.mem.MemSessionRepo
import pt.isel.ls.services.SessionServices
import pt.isel.ls.utils.INITIAL_GAME_ID
import pt.isel.ls.utils.generateGameDetails
import pt.isel.ls.utils.generateNameAndEmail
import java.time.format.DateTimeParseException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class SessionTests {
    private val pRepo = MemPlayersRepo()
    private val gRepo = MemGamesRepo()

    @BeforeTest
    fun setup() {
        val playerCount = 3
        val gameCount = 4
        (0 until playerCount)
            .map { generateNameAndEmail() }
            .forEach { pRepo.createPlayer(it.first, it.second) }

        (0 until gameCount)
            .map { generateGameDetails() }
            .forEach { gRepo.insert(it.name, it.dev, it.genres) }

    }

    @Test
    fun `creating a session successfully`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = INITIAL_GAME_ID
        val capacity = 5
        val startDate = "2024-04-03 18:40:00"

        //act
        val fPlayer = pRepo.getPlayer(1) ?: throw Exception("Error occured")
        val token = fPlayer.token
        val sid = sServices.createSession(token, gid, capacity, startDate)
        val session = sRepo.getSession(sid)

        //assert
        assertTrue(sid > 0 && session.state == State.OPEN && session.capacity == capacity && session.game == gid)

    }

    @Test
    fun `creating a session with a non-existing game`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = INITIAL_GAME_ID * 10
        val capacity = 5
        val startDate = "2024-04-03 18:40:00"

        //act && assert
        val fPlayer = pRepo.getPlayer(1) ?: throw Exception("Error occured")
        val token = fPlayer.token

        assertFailsWith<DomainException.GameNotFound> {
            sServices.createSession(token, gid, capacity, startDate)
        }

    }

    @Test
    fun `creating a session with a capacity lower than the lower bound`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = INITIAL_GAME_ID
        val capacity = 0
        val startDate = "2024-04-03 18:40:00"

        //act && assert
        val fPlayer = pRepo.getPlayer(1) ?: throw Exception("Error occured")
        val token = fPlayer.token

        assertFailsWith<IllegalStateException> {
            sServices.createSession(token, gid, capacity, startDate)
        }

    }

    @Test
    fun `creating a session with a capacity higher than the upper bound`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = INITIAL_GAME_ID
        val capacity = 100
        val startDate = "2024-04-03 18:40:00"

        //act && assert
        val fPlayer = pRepo.getPlayer(1) ?: throw Exception("Error occured")
        val token = fPlayer.token

        assertFailsWith<IllegalStateException> {
            sServices.createSession(token, gid, capacity, startDate)
        }

    }

    @Test
    fun `creating a session with a wrong formatted date`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = INITIAL_GAME_ID
        val capacity = 5
        val startDate = "2019-04-03 184000"

        //act && assert
        val fPlayer = pRepo.getPlayer(1) ?: throw Exception("Error occured")
        val token = fPlayer.token

        assertFailsWith<DateTimeParseException> {
            sServices.createSession(token, gid, capacity, startDate)
        }

    }

    @Test
    fun `creating a session with a date in the past`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = INITIAL_GAME_ID
        val capacity = 5
        val startDate = "2019-04-03 18:40:00"

        //act && assert
        val fPlayer = pRepo.getPlayer(1) ?: throw Exception("Error occured")
        val token = fPlayer.token

        assertFailsWith<DomainException.IllegalDate> {
            sServices.createSession(token, gid, capacity, startDate)
        }

    }



}
