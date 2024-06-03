package pt.isel.ls.domain

import pt.isel.ls.server.api.DEFAULT_MAX_LIMIT
import pt.isel.ls.server.api.DEFAULT_MIN_LIMIT
import pt.isel.ls.server.data.DataMem
import pt.isel.ls.server.services.Services
import kotlin.test.*

class GameTests {
    private val services = Services(DataMem())

    @Test
    fun `Testing listing games with a specific genre and developer`() {
        val gameList = services.listGames("",setOf(Genre.ACTION), "Test1", DEFAULT_MAX_LIMIT, DEFAULT_MIN_LIMIT)
        assertEquals( "game1", gameList[0].gameName)
        assertEquals( "Test1", gameList[0].gameDev)
        println("Games with genre ACTION and developer Test1: $gameList")
    }

    @Test
    fun `Test getting the details of a specific game given the ID`() {
        val game = services.createGame("GTA", "Rockstar", setOf(Genre.ACTION, Genre.ADVENTURE))
        val gameDetails = services.getGameDetails(game.toUInt())
        assertEquals("GTA", gameDetails?.gameName)
        assertEquals("Rockstar", gameDetails?.gameDev)
        assertEquals( setOf(Genre.ACTION,Genre.ADVENTURE), gameDetails?.gameGenre)
    }

    @Test
    fun `Test creating a game`() {
        val gameCreated = services.createGame("123", "321", setOf(Genre.ACTION, Genre.ADVENTURE))
        val getGame = services.getGameDetails(gameCreated.toUInt())
        assertEquals("123", getGame?.gameName)
        assertEquals("321", getGame?.gameDev)
        assertEquals( setOf(Genre.ACTION,Genre.ADVENTURE), getGame?.gameGenre)
    }

    @Test
    fun `Test listing games with a specific genre`() {
        val gameList = services.listGames("",setOf(Genre.ACTION), "", DEFAULT_MAX_LIMIT, DEFAULT_MIN_LIMIT)
        assertEquals( "game1", gameList[0].gameName)
        assertEquals( "Test1", gameList[0].gameDev)
        println("Games with genre ACTION: $gameList")
    }

    @Test
    fun `Test listing games with a specific developer`() {
        val gameList = services.listGames("",setOf(), "Test1", DEFAULT_MAX_LIMIT, DEFAULT_MIN_LIMIT)
        assertEquals( "game1", gameList[0].gameName)
        assertEquals( "Test1", gameList[0].gameDev)
        println("Games with developer Test1: $gameList")
    }

    @Test
    fun `Test listing games with a specific name`() {
        val gameList = services.listGames("game1",setOf(), "", DEFAULT_MAX_LIMIT, DEFAULT_MIN_LIMIT)
        assertEquals( "game1", gameList[0].gameName)
        assertEquals( "Test1", gameList[0].gameDev)
        println("Games with name game1: $gameList")
    }

    @Test
    fun `Test listing games with a specific name, genre and developer`() {
        val gameList = services.listGames("game1",setOf(Genre.ACTION), "Test1", DEFAULT_MAX_LIMIT, DEFAULT_MIN_LIMIT)
        assertEquals( "game1", gameList[0].gameName)
        assertEquals( "Test1", gameList[0].gameDev)
        println("Games with name game1, genre ACTION and developer Test1: $gameList")
    }


}
