package pt.isel.ls.repo.mem.games

import pt.isel.ls.repo.mem.MemGamesRepo
import pt.isel.ls.utils.FIRST_GAME_ID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class RepoMemGamesTests {
    @Test
    fun `check game exists by id`() {
        val repo = MemGamesRepo()
        val gId = repo.insert("name", "developer", listOf("genre"))

        assertEquals(FIRST_GAME_ID, gId)
        assertTrue { repo.checkGameExistsById(gId) }
        assertFalse { repo.checkGameExistsById(gId - 1) }
    }

    @Test
    fun `check game exists by name`() {
        val repo = MemGamesRepo()
        val gName = "name"
        repo.insert(gName, "developer", listOf("genre"))

        assertTrue { repo.checkGameExistsByName(gName) }
        assertTrue { repo.checkGameExistsByName(gName.uppercase()) } //test case-insensitive
        assertFalse { repo.checkGameExistsByName("anotherName") }
    }

    @Test
    fun `insert two games`() {
        val repo = MemGamesRepo()
        val gId1 = repo.insert("name", "developer", listOf("genre"))

        assertEquals(FIRST_GAME_ID, gId1)

        val gId2 = repo.insert("name", "developer", listOf("genre"))
        assertEquals(FIRST_GAME_ID + 1, gId2)
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
    fun `get list of games searched by name`() {
        val repo = MemGamesRepo()
        val name1 = "FIFA 19"
        val name2 = "FIFA 20"
        val name3 = "FIFA 21"
        val name4 = "Football Manager 2021"

        val gameId1 = repo.insert(name1, "developer1", listOf("genreA", "genreB"))
        val game1 = repo.getGameById(gameId1)
        val gameId2 = repo.insert(name2, "developer2", listOf("genreB", "genreC"))
        val game2 = repo.getGameById(gameId2)
        val gameId3 = repo.insert(name3, "developer3", listOf("genreC", "genreD"))
        val game3 = repo.getGameById(gameId3)
        val gameId4 = repo.insert(name4, "developer4", listOf("genreD", "genreE"))
        val game4 = repo.getGameById(gameId4)

        val (list1, _) = repo.getGamesByName("F", 5, 0)
        assertTrue { list1.containsAll(listOf(game1, game2, game3, game4)) && list1.size == 4 }
        val (list2, _) = repo.getGamesByName("FIFA", 5, 0)
        assertTrue { list2.containsAll(listOf(game1, game2, game3)) && list2.size == 3 }
        val (list3, _) = repo.getGamesByName("fifa 2", 5, 0)
        assertTrue { list3.containsAll(listOf(game2, game3)) && list3.size == 2 }
        val (list4, _) = repo.getGamesByName("Football Manager", 5, 0)
        assertTrue { list4.contains(game4) && list4.size == 1 }
        val (list5, _) = repo.getGamesByName("21", 5, 0)
        assertTrue { list5.containsAll(listOf(game3, game4)) && list5.size == 2 }
        val (list6, _) = repo.getGamesByName("F", 2, 1)
        assertTrue { list6.containsAll(listOf(game2, game3)) && list6.size == 2 }
    }

    @Test
    fun `get list of games by dev and genre(s)`() {
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

        val (list1, _) = repo.getListOfGames(genresABC, dev1, 5, 0)
        assertTrue { list1.containsAll(listOf(game1, game2, game3)) && list1.size == 3 }

        val (list2, _) = repo.getListOfGames(genresXYZ, dev1, 5, 0)
        assertTrue { list2.containsAll(listOf(game1, game2, game3, game4)) && list2.size == 4 }

        val (list3, _) = repo.getListOfGames(genresBCD, dev2, 5, 0)
        assertTrue { list3.containsAll(listOf(game1, game2, game4)) && list3.size == 3 }

        val (list4, _) = repo.getListOfGames(genresXYZ, dev2, 5, 0)
        assertTrue { list4.containsAll(listOf(game3, game4)) && list4.size == 2 }

        val (list5, _) = repo.getListOfGames(listOf(genresABC[2]), dev1, 5, 0)
        assertTrue { list5.containsAll(listOf(game1, game2, game3)) && list5.size == 3 }

        val (list6, _) = repo.getListOfGames(listOf(genresXYZ[0].uppercase()), dev2, 5, 0)
        assertTrue { list6.containsAll(listOf(game3, game4)) && list6.size == 2 }

        val (list7, _) = repo.getListOfGames(listOf("G"), "developer3", 5, 0)
        assertTrue { list7.isEmpty() }

        val (list8, _) = repo.getListOfGames(genresABC, "developer4", 5, 0)
        assertTrue { list8.containsAll(listOf(game1, game2)) && list8.size == 2 }

        val (list9, _) = repo.getListOfGames(listOf("H"), dev2, 5, 0)
        assertTrue { list9.containsAll(listOf(game4)) && list9.size == 1 }

        val (list10, _) = repo.getListOfGames(listOf(genresXYZ[0]), "", 5, 0)
        assertTrue { list10.containsAll(listOf(game3, game4)) && list10.size == 2 }

        val (list11, _) = repo.getListOfGames(listOf(), dev1, 5, 0)
        assertTrue { list11.containsAll(listOf(game1, game2, game3)) && list11.size == 3 }

        val (list12, _) = repo.getListOfGames(listOf(), "", 5, 0)
        assertTrue { list12.containsAll(listOf(game1, game2, game3, game4)) && list12.size == 4 }

        val (list13, _) = repo.getListOfGames(listOf(""), "", 5, 0)
        assertTrue { list13.containsAll(listOf(game1, game2, game3, game4)) && list13.size == 4 }

        val (list14, _) = repo.getListOfGames(listOf(""), dev2, 5, 0)
        assertTrue { list14.containsAll(listOf(game4)) && list14.size == 1 }

        val (list15, _) = repo.getListOfGames(listOf(), "", 2, 1)
        assertTrue { list15.containsAll(listOf(game2, game3)) && list15.size == 2 }
    }

    @Test
    fun `paging - use of limit and skip`() {
        val repo = MemGamesRepo()
        val dev = "developer"
        val genresABC = listOf("genreA", "genreB", "genreC")
        val genresBCD = listOf("genreB", "genreC", "genreD")

        val gameId1 = repo.insert("name1", dev, genresABC)
        val gameId2 = repo.insert("name2", dev, genresABC)
        val gameId3 = repo.insert("name3", dev, genresBCD)
        val gameId4 = repo.insert("name4", dev, genresBCD)
        val gameId5 = repo.insert("name5", dev, genresABC)
        val gameId6 = repo.insert("name6", dev, genresABC)
        val gameId7 = repo.insert("name7", dev, genresBCD)
        val gameId8 = repo.insert("name8", "devJ", genresBCD)
        val gameId9 = repo.insert("name9", dev, genresABC)
        val gameId10 = repo.insert("name10", "devP", genresABC)

        val (list1, _) = repo.getListOfGames(genresABC, dev, 3, 0)
        assertTrue {
            list1.size == 3 && list1.containsAll(
                listOf(
                    repo.getGameById(gameId1),
                    repo.getGameById(gameId2),
                    repo.getGameById(gameId3)
                )
            )
        }

        val (list2, _) = repo.getListOfGames(genresABC, dev, 3, 3)
        assertTrue {
            list2.size == 3 && list2.containsAll(
                listOf(
                    repo.getGameById(gameId4),
                    repo.getGameById(gameId5),
                    repo.getGameById(gameId6)
                )
            )
        }

        val (list3, _) = repo.getListOfGames(genresABC, dev, 3, 6)
        assertTrue {
            list3.size == 3 && list3.containsAll(
                listOf(
                    repo.getGameById(gameId7),
                    repo.getGameById(gameId8),
                    repo.getGameById(gameId9)
                )
            )
        }

        val (list4, _) = repo.getListOfGames(genresABC, dev, 3, 9)
        //limit is 3 but there is only one game left
        assertTrue { list4.size == 1 && list4.contains(repo.getGameById(gameId10)) }

        val (list5, _) = repo.getListOfGames(genresABC, dev, 3, 10)
        //skip is 10 but there are no games left
        assertTrue { list5.isEmpty() }
    }
}