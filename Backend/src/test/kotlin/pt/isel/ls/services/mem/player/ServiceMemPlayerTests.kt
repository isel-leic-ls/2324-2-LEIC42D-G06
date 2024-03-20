package pt.isel.ls.services.mem.player

import junit.framework.TestCase.assertEquals
import org.junit.Test
import pt.isel.ls.repo.DomainException
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.utils.CAPACITY_LOWER_BOUND
import pt.isel.ls.utils.CAPACITY_UPPER_BOUND
import pt.isel.ls.utils.FIRST_PLAYER_ID
import kotlin.test.assertFailsWith

class ServiceMemPlayerTests {

    @Test
    fun `createPlayer should throw IllegalArgumentException when given name is empty`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer("", "email")
        }
        // Assert
        assertEquals("Name cannot be empty", exception.message)
    }

    @Test
    fun `createPlayer should throw IllegalArgumentException when given email is empty`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer("name", "")
        }
        // Assert
        assertEquals("Email cannot be empty", exception.message)
    }

    @Test
    fun `createPlayer should throw IllegalArgumentException when given name length is less than 2`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer("n", "email")
        }
        // Assert
        assertEquals(
            "Name length must be between $CAPACITY_LOWER_BOUND and $CAPACITY_UPPER_BOUND", exception.message
        )
    }

    @Test
    fun `createPlayer should throw IllegalArgumentException when given name length is greater than 20`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer("nameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", "email")
        }
        // Assert
        assertEquals(
            "Name length must be between $CAPACITY_LOWER_BOUND and $CAPACITY_UPPER_BOUND", exception.message
        )
    }

    @Test
    fun `createPlayer should throw IllegalArgumentException when given invalid email length`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer("name", "e")
        }
        // Assert
        assertEquals(
            "Invalid email", exception.message
        )
    }

    @Test
    fun `getPlayer should throw IllegalArgumentException when given pid is less than 1`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.getPlayer(0)
        }
        // Assert
        assertEquals("Player id must be greater or equal to $FIRST_PLAYER_ID", exception.message)
    }

    @Test
    fun `getPlayer should throw exception when given pid is not found`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act & Assert
        assertFailsWith<DomainException.PlayerNotFound> { service.getPlayer(FIRST_PLAYER_ID) }
    }

    @Test
    fun `getPlayerIdByToken should throw IllegalArgumentException when given token is empty`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.getPlayerIdByToken("")
        }
        // Assert
        //TODO assertEquals("Token cannot be empty", exception.message)
    }

    @Test
    fun `getPlayerIdByToken should throw IllegalArgumentException when given token length is greater than 36`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.getPlayerIdByToken("123456789012345678901234567890123456")
        }
        // Assert
        assertEquals("Token length must be less than 36", exception.message)
    }

}