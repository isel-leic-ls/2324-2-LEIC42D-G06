package pt.isel.ls.repo.mem.names

/*import pt.isel.ls.repo.mem.MemGamesRepo
import kotlin.test.Test
import kotlin.test.assertEquals


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
        val gameId2 = repo.insert("name2", dev1, genresBCD)
        val gameId3 = repo.insert("name3", dev1, genresXYZ)
        val gameId4 = repo.insert("name4", dev2, genresXYZ)

        val list1 = repo.getListOfGames(genresABC, dev1)
    }
}*/