package pt.isel.ls.http.dataDB

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.server.api.WebApi
import pt.isel.ls.server.data.DataBD
import pt.isel.ls.server.services.Services
import kotlin.test.Test

/*

class PlayerRequestDBTest {
    private val webApi = WebApi(Services(DataBD()))

    @Test
    fun `Create a new player in DB`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .body("""{"name": "jogador1", "username": "jogador1","email": "j1@gmail.com", "password":"isel"}""")
        // Act
        val response = { _: Request -> webApi.createPlayer(request) }
        val ret = response(request)
        // Assert
        assert(ret.status == Status.CREATED)
    }
    @Test
    fun `player details by passing an id in DB`() {
        val id = "1"
        // Arrange
        val request = Request(Method.GET, "/players/$id")
        // Act
        val response = webApi.getDetailsOfPlayer(request)
        // Assert

        assert(response.header("content-type") == "application/json")
        println("DETAILS OF PLAYERID $id:" + response.bodyString())

    }

    @Test
    fun `List all players in DB`() {
        // Arrange
        val request = Request(Method.GET, "/players/list")
        // Act
        val response = webApi.listPlayers(request)
        // Assert
        assert(response.header("content-type") == "application/json")
    }
}

 */