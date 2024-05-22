package pt.isel.ls.services.mem.player

import junit.framework.TestCase.assertEquals
import org.junit.Test
import pt.isel.ls.AppException
import pt.isel.ls.repo.mem.MemPlayersRepo
import pt.isel.ls.services.PlayerServices
import pt.isel.ls.utils.MIN_NAME_LENGTH
import pt.isel.ls.utils.FIRST_PLAYER_ID
import pt.isel.ls.utils.MAX_NAME_LENGTH
import kotlin.test.assertFailsWith

class ServiceMemPlayerTests {

    @Test
    fun `createPlayer should throw IllegalArgumentException when given name is empty`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer("", "email", "vasco123")
        }
        // Assert
        assertEquals("Name cannot be blank", exception.message)
    }

    @Test
    fun `createPlayer should throw IllegalArgumentException when given email is empty`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer("name", "", "vasco123")
        }
        // Assert
        assertEquals("Invalid email", exception.message)
    }

    @Test
    fun `createPlayer should throw IllegalArgumentException when given name length is less than 2`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer("n", "email", "vasco123")
        }
        // Assert
        assertEquals(
            "Name length must be between $MIN_NAME_LENGTH and $MAX_NAME_LENGTH", exception.message
        )
    }

    @Test
    fun `createPlayer should throw IllegalArgumentException when given name length is greater than 20`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer(
                "nameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
                "name@gmail.com",
                "vasco123"
            )
        }
        // Assert
        assertEquals(
            "Name length must be between $MIN_NAME_LENGTH and $MAX_NAME_LENGTH", exception.message
        )
    }

    @Test
    fun `createPlayer should throw IllegalArgumentException when given invalid email length`() {
        // Arrange
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        // Act
        val exception = assertFailsWith<IllegalArgumentException> {
            service.createPlayer("name", "e", "vasco123")
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

    @Test
    fun `get player by username with invalid skip`() {
       val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        assertFailsWith<IllegalArgumentException> { service.getPlayersByUsername("Vasco",-1,1) }
    }

    @Test
    fun `get player by username with invalid limit`() {
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        assertFailsWith<IllegalArgumentException> { service.getPlayersByUsername("Vasco",1,-1) }
    }

    @Test
    fun `get player by username with invalid name`() {
        val pRepo = MemPlayersRepo()
        val service = PlayerServices(pRepo)
        val username = buildString {
            repeat(51){append("a")}
        }
        assertFailsWith<IllegalArgumentException> { service.getPlayersByUsername(username,1,-1) }
    }


}