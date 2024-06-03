package pt.isel.ls.http.dataMem
/*
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
import pt.isel.ls.server.api.WebApi
import pt.isel.ls.server.data.DataMem
import pt.isel.ls.server.services.Services

import kotlin.test.*

class PlayerRequestsTest {
    private val webApi = WebApi(Services(DataMem()))

    @Test
    fun `creates a new player`() {
        val request = Request(Method.POST, "/players")
            .body("""{"name": "Gonçalo", "username": "goncalo","email": "goncalo@gmail.com"}""")

        val response = webApi.createPlayer(request)

        assert(response.status == Status.CREATED)
    }

    @Test
    fun `gets details of a player`() {
        val request = Request(Method.GET, "/players/1")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/players/{id}"))

        assert(webApi.getDetailsOfPlayer(routedRequest).status == Status.OK)
    }


    @Test
    fun `lists players`() {
        val request = Request(Method.GET, "/players")

        val response = webApi.listPlayers(request)

        assert(response.status == Status.OK)
        assertEquals("application/json", response.header("content-type"))
    }

    @Test
    fun `get list of players with limit and skip`() {
        val request = Request(Method.GET, "/players/list?limit=2&skip=0")

        val response = webApi.listPlayers(request)

        assert(response.status == Status.OK)
        assertEquals("application/json", response.header("content-type"))
    }

}
*/