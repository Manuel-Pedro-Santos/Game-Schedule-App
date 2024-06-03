package pt.isel.ls.http.dataMem


import org.http4k.core.*
import org.http4k.routing.RoutedRequest
import pt.isel.ls.server.api.*
import pt.isel.ls.server.data.DataMem
import pt.isel.ls.server.services.Services
import kotlin.test.*


@Suppress("NAME_SHADOWING")
class SessionRequestTests {
    private val webApi = WebApi(Services(DataMem()))

    @Test
    fun `creates a new session`() {
        val request = Request(Method.POST, "/sessions/createSession")
            .header("Authorization", "Bearer c04c8d06-a64b-494c-b49b-2a484cdbc57a")
            .body("""{"playerID": 1, "capacity": 3,"gameName": "game1","date": "2024-03-08T18:43:31"}""")

        assertEquals(201, webApi.createNewSession(request).status.code)
    }

    @Test
    fun `adds a player to a session`() {
        val request = Request(Method.POST, "/sessions/addPlayer")
            .header("Authorization", "Bearer c04c8d06-a64b-494c-b49b-2a484cdbc57a")
            .body("""{"id": 3, "sessionId": 3}""")
        val statusCode = webApi.addPLayerToSession(request).status.code
        assertEquals(200, statusCode)
    }


    @Test
    fun `gets the details of a session`() {
        val request = Request(Method.GET, "/sessions/1")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{id}"))
        assertEquals(200, webApi.getSessionDetails(routedRequest).status.code)
    }


    @Test
    fun `get list sessions`() {
        val request = Request(Method.GET, "/sessions/listSessions/1")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/listSessions/{id}"))
        assertEquals(200, webApi.getListSessions(routedRequest).status.code)
    }


    @Test
    fun `gets an empty list of sessions`() {
        // Remove all sessions
        val sessionsRequest = Request(Method.GET, "/sessions/listSessions/2200")
        val sessionsResponse = webApi.getListSessions(sessionsRequest)
        assertEquals(400, sessionsResponse.status.code)
    }


}
