package pt.isel.ls.domain

import pt.isel.ls.server.data.DataMem
import pt.isel.ls.server.data.playersList
import pt.isel.ls.server.services.Services

import kotlin.test.*

class PlayerTests {
    private val service = Services(DataMem())
    @Test
    fun testCreatePlayer() {

        val player = service.createPlayer(name = "ISEL", username ="isel",  email = "ISEL@gmail.com", password = "isel")
        assertEquals(9u, player.second.toUInt())
        println(playersList)
    }

    @Test
    fun testGetPlayerById() {
        val playerId = service.getPlayerById(9U)
        assertEquals(9U,playerId?.id)
        assertEquals("ISEL", playerId?.name)
        assertEquals("isel", playerId?.username)
        assertEquals("ISEL@gmail.com", playerId?.email)
    }

    @Test
    fun testGetPlayerByUsername() {
        val player = service.getPlayerByUsername("goncalo")
        assertEquals(1U, player?.id)
    }

    @Test
    fun testGetListOfPlayers() {
        val players = service.getListOfPlayers(9, 0)
        assert(players.isNotEmpty())
        assertEquals(9, players.size)
        assertEquals(1U, players[0].id)
    }

    @Test
    fun testGetPlayerByEmail() {
        val player = service.getListOfPlayers(1, 0)
        assertEquals(
            mutableListOf(
                PlayerDC(1u, "Gonçalo", "goncalo", "goncalo@gmail.com", "isel", "4d259054-dbfc-4f5d-945e-b5c33b89a2c5"),
            ), player
        )
    }
}