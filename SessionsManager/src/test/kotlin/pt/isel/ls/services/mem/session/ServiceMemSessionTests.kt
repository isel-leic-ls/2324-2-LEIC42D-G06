package pt.isel.ls.services.mem.session

import pt.isel.ls.domain.createSessionDTO
import pt.isel.ls.AppException
import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.repo.mem.MemSessionRepo
import pt.isel.ls.services.SessionServices
import pt.isel.ls.utils.*
import java.time.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class ServiceMemSessionTests {
    private val pRepo = MemPlayersRepo()
    private val gRepo = MemGamesRepo()

    @BeforeTest
    fun setup() {
        val playerCount = 3
        val gameCount = 4
        (0 until playerCount)
            .map { generatePlayerDetails() }
            .forEach { (n, e, t, p) -> pRepo.createPlayer(n, e, t, p) }

        (0 until gameCount)
            .map { generateGameDetails() }
            .forEach { gRepo.insert(it.name, it.dev, it.genres) }

    }

    @Test
    fun `creating a session successfully`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = FIRST_GAME_ID
        val capacity = 5
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER)

        //act
        val fPlayer = pRepo.getPlayer(FIRST_PLAYER_ID)
        val token = fPlayer.token
        val sid = sServices.createSession(token, gid, capacity, startDate)
        val session = sRepo.getSession(sid)

        //assert
        assertTrue(sid > 0 && !session.closed && session.capacity == capacity && session.game == gid)

    }

    @Test
    fun `creating a session with a non-existing game`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = FIRST_GAME_ID * 10
        val capacity = 5
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER)

        //act && assert
        val fPlayer = pRepo.getPlayer(FIRST_PLAYER_ID)
        val token = fPlayer.token

        assertFailsWith<AppException.GameNotFound> {
            sServices.createSession(token, gid, capacity, startDate)
        }

    }

    @Test
    fun `creating a session with a capacity lower than the lower bound`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = FIRST_GAME_ID
        val capacity = 0
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER)

        //act && assert
        val fPlayer = pRepo.getPlayer(FIRST_PLAYER_ID)
        val token = fPlayer.token

        assertFailsWith<IllegalArgumentException> {
            sServices.createSession(token, gid, capacity, startDate)
        }

    }

    @Test
    fun `creating a session with a wrong formatted date`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = FIRST_GAME_ID
        val capacity = 5
        val startDate = "2019-04-03 184000"

        //act && assert
        val fPlayer = pRepo.getPlayer(FIRST_PLAYER_ID)
        val token = fPlayer.token

        assertFailsWith<IllegalArgumentException> {
            sServices.createSession(token, gid, capacity, startDate)
        }

    }

    @Test
    fun `creating a session with a date in the past`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = FIRST_GAME_ID
        val capacity = 5
        val startDate = LocalDateTime.now().minusDays(1).format(DATE_TIME_FORMATTER)

        //act && assert
        val fPlayer = pRepo.getPlayer(FIRST_PLAYER_ID)
        val token = fPlayer.token

        assertFailsWith<IllegalArgumentException> {
            sServices.createSession(token, gid, capacity, startDate)
        }

    }

    @Test
    fun `adding player to a session successful case`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val pid = FIRST_PLAYER_ID
        val gid = FIRST_GAME_ID
        val capacity = 5
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER)
        val sid = sRepo.createSession(createSessionDTO(capacity, startDate, gid, listOf(pid)))

        //act
        val fPlayer = pRepo.getPlayer(FIRST_PLAYER_ID + 1)
        val token = fPlayer.token

        sServices.addPlayerToSession(token, sid)
        val session = sRepo.getSession(sid)

        //assert
        assertTrue(session.players.containsAll(listOf(pid, fPlayer.id)))
    }

    @Test
    fun `adding a player to a session which he already is apart of`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val pid = FIRST_PLAYER_ID
        val gid = FIRST_GAME_ID
        val capacity = 5
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER)
        val sid = sRepo.createSession(createSessionDTO(capacity, startDate, gid, listOf(pid)))

        //act
        val fPlayer = pRepo.getPlayer(pid)
        val token = fPlayer.token

        assertFailsWith<AppException.PlayerAlreadyInSession> {
            sServices.addPlayerToSession(token, sid)
        }
    }

    @Test
    fun `adding a player to a session which is closed`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val pid = FIRST_PLAYER_ID
        val gid = FIRST_GAME_ID
        val capacity = 2
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER)
        val sid = sRepo.createSession(createSessionDTO(capacity, startDate, gid, listOf(pid)))

        sRepo.addPlayerToSession(sid, FIRST_PLAYER_ID + 1)

        //act
        val fPlayer = pRepo.getPlayer(FIRST_PLAYER_ID + 2)
        val token = fPlayer.token

        assertFailsWith<AppException.SessionClosed> {
            sServices.addPlayerToSession(token, sid)
        }
    }

    @Test
    fun `getting a session successfully`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val pid = FIRST_PLAYER_ID
        val gid = FIRST_GAME_ID
        val capacity = 5
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER)
        val sid = sRepo.createSession(createSessionDTO(capacity, startDate, gid, listOf(pid)))

        //act
        val session = sServices.getSession(sid)

        //assert
        assertTrue(session.id == sid && session.capacity == capacity && session.game == gid)
    }

    @Test
    fun `querying sessions with badly formatted date`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val gid = FIRST_GAME_ID
        val startDate = "2019-04-03 184000"

        //act && assert
        assertFailsWith<IllegalArgumentException> {
            sServices.getListOfSessions(gid, startDate, null, null, 0, 10)
        }
    }

    @Test
    fun `updating a session successfully`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val pid = FIRST_PLAYER_ID + 1
        val gid = FIRST_GAME_ID
        val capacity = 5
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER)
        val sid = sRepo.createSession(createSessionDTO(capacity, startDate, gid, listOf(pid)))

        //act
        val fPlayer = pRepo.getPlayer(FIRST_PLAYER_ID + 1)
        val token = fPlayer.token
        val newCapacity = 10
        val newStartDate = LocalDateTime.now().plusDays(2).format(DATE_TIME_FORMATTER)

        sServices.updateSession(token, sid, newStartDate, newCapacity)
        val session = sRepo.getSession(sid)

        //assert
        assertTrue(session.capacity == newCapacity && session.date == newStartDate)
    }

    @Test
    fun `deleting session successfully`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val pid = FIRST_PLAYER_ID + 1
        val gid = FIRST_GAME_ID
        val capacity = 5
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER)
        val sid = sRepo.createSession(createSessionDTO(capacity, startDate, gid, listOf(pid)))

        //act
        val fPlayer = pRepo.getPlayer(FIRST_PLAYER_ID + 1)
        val token = fPlayer.token
        sServices.deleteSession(token, sid)

        //assert
        assertFailsWith<AppException.SessionNotFound> {
            sRepo.getSession(sid)
        }
    }

    @Test
    fun `deleting the only player in a session`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val pid = FIRST_PLAYER_ID + 1
        val gid = FIRST_GAME_ID
        val capacity = 5
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER)
        val sid = sRepo.createSession(createSessionDTO(capacity, startDate, gid, listOf(pid)))

        //act
        val fPlayer = pRepo.getPlayer(FIRST_PLAYER_ID + 1)
        val token = fPlayer.token
        sServices.deletePlayerFromSession(token, sid)

        //assert
        assertFailsWith<AppException.SessionNotFound> {
            sRepo.getSession(sid)
        }
    }

    @Test
    fun `joining a session successfully`() {
        //arrange
        val sRepo = MemSessionRepo()
        val sServices = SessionServices(pRepo, gRepo, sRepo)
        val pid = FIRST_PLAYER_ID
        val gid = FIRST_GAME_ID
        val capacity = 5
        val startDate = LocalDateTime.now().plusDays(1).format(DATE_TIME_FORMATTER)
        val sid = sRepo.createSession(createSessionDTO(capacity, startDate, gid, listOf(pid)))

        //act
        val fPlayer = pRepo.getPlayer(FIRST_PLAYER_ID + 1)
        val token = fPlayer.token
        sServices.addPlayerToSession(token, sid)
        val session = sRepo.getSession(sid)

        //assert
        assertTrue(session.players.containsAll(listOf(pid, fPlayer.id)))
    }
}