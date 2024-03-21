package pt.isel.ls.repo.mem.players

import junit.framework.TestCase.assertEquals
import org.junit.Test
import pt.isel.ls.AppException
import pt.isel.ls.repo.mem.MemPlayersRepo
import kotlin.test.assertFailsWith


class RepoMemPlayerTests {


    @Test
    fun `create a player`() {
        val repo = MemPlayersRepo()
        val name = "Vasco Branco"
        val email = "vascobranco13@gmail.com"
        val token = "f907d1b0-105b-455d-acc2-8422a2056f1d"
        val pid = repo.createPlayer(name, email, token,"vasco123")
        val player = repo.getPlayer(pid)
        assertEquals(pid, player.id)
    }

    @Test
    fun `create two players`() {
        val repo = MemPlayersRepo()
        val name = "Vasco Branco"
        val email = "vascobranco13@gmail.com"
        val token = "f907d1b0-105b-455d-acc2-8422a2056f1d"
        val name2 = "Pedro Diz"
        val email2 = "joaopereira@gmail.com"
        val token2 = "f907d1b0-105b-455d-acc2-8422a2056f3d"
        val pid = repo.createPlayer(name, email, token,"vasco123")
        val pid2 = repo.createPlayer(name2, email2, token2,"pedro123")
        assertEquals(pid + 1, pid2)
    }

    @Test
    fun `get player by id`() {
        val repo = MemPlayersRepo()
        val name = "Pedro Diz"
        val email = "pedrodiz@gmail.com"
        val token = "f907d1b0-105b-455d-acc2-8422a2056f2d"
        val pid = repo.createPlayer(name, email, token,"pedro123")
        val player = repo.getPlayer(pid)
        assertEquals(name, player.name)
        assertEquals(email, player.email)
        assertEquals(token, player.token)
    }

    @Test
    fun `get player id by token`() {
        val repo = MemPlayersRepo()
        val name = "João Pereira"
        val email = "joaopereira@gmail.com"
        val token = "f907d1b0-105b-455d-acc2-8422a2056f3d"
        val pid = repo.createPlayer(name, email, token,"joao123")
        val pid2 = repo.getPlayerIdByToken(token)
        assertEquals(pid, pid2)
    }

    @Test
    fun `get nonexistent player by id`() {
        val repo = MemPlayersRepo()
        val pid = 55
        assertFailsWith<AppException.PlayerNotFound>{repo.getPlayer(pid)}
    }

    @Test
    fun `get nonexistent player by token`() {
        val repo = MemPlayersRepo()
        val token = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        assertFailsWith<AppException.PlayerNotFound>{repo.getPlayerIdByToken(token)}

    }
}

