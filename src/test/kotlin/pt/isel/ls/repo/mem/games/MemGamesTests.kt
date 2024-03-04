package pt.isel.ls.repo.mem.games

import pt.isel.ls.repo.mem.MemGamesRepo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class MemGamesTests {
    @Test
    fun `insert game`() {
        val repo = MemGamesRepo()
        val gid = repo.insert("name", "developer", listOf("genre"))

        assertEquals(0, gid)
        assertEquals(1, repo.games.size)
    }

    @Test
    fun `get game by id`() {
        val repo = MemGamesRepo()
        val name = "name"
        val dev = "developer"
        val genres = listOf("genreA", "genreB")

        val gameId = repo.insert(name, dev, genres)
        val game = repo.getGameById(gameId)

        assertEquals(gameId, game.id)
        assertEquals(name, game.name)
        assertEquals(dev, game.dev)
        assertEquals(genres, game.genres)
    }

    @Test
    fun `get game by name`() {
        val repo = MemGamesRepo()
        val name = "name"
        val dev = "developer"
        val genres = listOf("genreA", "genreB")

        val gameId = repo.insert(name, dev, genres)
        val game = repo.getGameByName(name)

        assertEquals(gameId, game.id)
        assertEquals(name, game.name)
        assertEquals(dev, game.dev)
        assertEquals(genres, game.genres)
    }

    @Test
    fun `get list of games`() {
        val repo = MemGamesRepo()
        val dev1 = "developer1"
        val dev2 = "developer2"
        val genresABC = listOf("genreA", "genreB", "genreC")
        val genresBCD = listOf("genreB", "genreC", "genreD")
        val genresXYZ = listOf("genreX", "genreY", "genreZ")

        val gameId1 = repo.insert("name1", dev1, genresABC)
        val game1 = repo.getGameById(gameId1)
        val gameId2 = repo.insert("name2", dev1, genresBCD)
        val game2 = repo.getGameById(gameId2)
        val gameId3 = repo.insert("name3", dev1, genresXYZ)
        val game3 = repo.getGameById(gameId3)
        val gameId4 = repo.insert("name4", dev2, genresXYZ)
        val game4 = repo.getGameById(gameId4)

        val list1 = repo.getListOfGames(genresABC, dev1)
        assertTrue { list1.containsAll(listOf(game1, game2, game3)) && list1.size == 3 }

        val list2 = repo.getListOfGames(genresXYZ, dev1)
        assertTrue { list2.containsAll(listOf(game1, game2, game3, game4)) && list2.size == 4 }

        val list3 = repo.getListOfGames(genresBCD, dev2)
        assertTrue { list3.containsAll(listOf(game1, game2, game4)) && list3.size == 3 }

        val list4 = repo.getListOfGames(genresXYZ, dev2)
        assertTrue { list4.containsAll(listOf(game3, game4)) && list4.size == 2 }

        val list5 = repo.getListOfGames(listOf(genresABC[2]), dev1)
        assertTrue { list5.containsAll(listOf(game1, game2, game3)) && list5.size == 3 }

        val list6 = repo.getListOfGames(listOf(genresXYZ[0]), dev2)
        assertTrue { list6.containsAll(listOf(game3, game4)) && list6.size == 2 }

        val list7 = repo.getListOfGames(listOf("G"), "developer3")
        assertTrue { list7.isEmpty() }

        val list8 = repo.getListOfGames(genresABC, "developer4")
        assertTrue { list8.containsAll(listOf(game1, game2)) && list8.size == 2 }

        val list9 = repo.getListOfGames(listOf("H"), dev2)
        assertTrue { list9.containsAll(listOf(game4)) && list9.size == 1 }
    }
}