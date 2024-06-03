package pt.isel.ls.domain

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.server.data.DataMem
import pt.isel.ls.server.data.listSessions
import pt.isel.ls.server.services.Services
import kotlin.test.*


class SessionsTests {

   private val service = Services(DataMem())

    @Test
    fun testCreateSession() {
        val session = service.createSession(5U, 1u, "game1", LocalDateTime(2024, 6, 3, 0, 0))
        val found = listSessions.find { it.id == session.toUInt() }
        println(LocalDateTime(2024, 6, 3, 0, 0))
        assertTrue { found != null }
    }


    @Test
    fun addPlayerToSession() {
        val sessionCreated = service.createSession(5U, 2u, "game1", LocalDateTime(2024, 6, 3, 0, 0))
        val session = service.addPlayerToSession(2u, sessionCreated.toUInt())
        val entered = session.associatedPlayers?.find { it.id == 2u}

        assertEquals(entered?.id,  2U)

    }

    @Test
    fun getDetailsOfSession() {
        service.getDetailsOfSession(1u)
        assertTrue { listSessions.find { it.id == 1u } != null }
    }



    @Test
    fun listSessions () {
        val wantedlists = service.getListSession(1u)
        assertTrue { wantedlists.isNotEmpty() }
    }
    @Test
    fun testRemoveSession() {
        // Create a session
        val sessionCreated = service.createSession(5U, 3u, "game1", LocalDateTime(2024, 6, 3, 0, 0))
        // Remove the session
        service.deleteSession(sessionCreated.toUInt())
        // Check if the session was removed
        val found = listSessions.find { it.id == sessionCreated.toUInt() }
        assertTrue { found == null }
    }

    @Test
    fun testEmptyListSessions() {
        // Remove all sessions
        listSessions.clear()
        // Check if the list of sessions is empty
        val sessions = service.getListSession(1u)
        assertTrue { sessions.isEmpty() }
    }

}
