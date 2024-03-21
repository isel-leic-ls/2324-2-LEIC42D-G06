package pt.isel.ls.services.mem.player

import junit.framework.TestCase.assertEquals
import org.junit.Test
import pt.isel.ls.AppException
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.utils.CAPACITY_LOWER_BOUND
import pt.isel.ls.utils.CAPACITY_UPPER_BOUND
import pt.isel.ls.utils.FIRST_PLAYER_ID
import kotlin.test.Ignore
import kotlin.test.assertFailsWith

class ServiceMemPlayerTests {

    @Test
    fun `createPlayer should throw IllegalArgumentException when given name is empty`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer("", "email","vasco123")
        }
        // Assert
        assertEquals("Name cannot be empty", exception.message)
    }

    //@Ignore //TODO Vasco verifica o assert se realmente queremos que seja este
    @Test
    fun `createPlayer should throw IllegalArgumentException when given email is empty`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer("name", "","vasco123")
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
            service.createPlayer("n", "email","vasco123")
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
            service.createPlayer("nameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", "email","vasco123")
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
            service.createPlayer("name", "e","vasco123")
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
        assertFailsWith<AppException.PlayerNotFound> { service.getPlayer(FIRST_PLAYER_ID) }
    }
}