package pt.isel.ls.http.dataDB

import org.http4k.core.*
import org.http4k.routing.RoutedRequest
import pt.isel.ls.server.api.*
import pt.isel.ls.server.data.*
import pt.isel.ls.server.services.Services
import kotlin.test.Test
/*

class GameRequestDBTest {
    private val webApi = WebApi(Services(DataBD()))
    /*
    @Test
    fun `Create a new game in DB`() {
        // Arrange
        val randomInt = (0..10).random()
        val request1 = Request(Method.POST, "/games/createGame")
            .body("""{"gameName": "jogoTeste${randomInt}","gameDev": "jogoTeste","gameGenre": "Adventure"}""")
        // Act
        val response = { request: Request -> webApi.createNewGame(request) }
        val ret = response(request1)
        // Assert
        assert(ret.status == Status.CREATED)
    }
    */


    @Test
    fun `Test to get game details by passing an id in DB`() {
        val id = "1"
        // Arrange
        val request = Request(Method.GET, "/games/details/$id")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/details/{id}"))
        // Act
        val response = webApi.detailsGame(routedRequest)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")

    }


    @Test
    fun `Test to get list games by passing a genre and a developer in DB`() {
        // Arrange
        val requestBody = """{"genre": "Action"}"""
        val request = Request(Method.GET, "/games/list/EA").body(requestBody).query("limit", "2")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/list/{dev}"))
        // Act
        val response = webApi.listGames(routedRequest)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
        println("LIST  OF GAMES:" + response.bodyString())
    }

    @Test
    fun `Search Game  by Name`() {
        val request = Request(Method.GET, "/games/list").query("gameName", "jo")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/list"))
        // Act
        val response = webApi.listGames(routedRequest)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
        println("LIST  OF GAMES:" + response.bodyString())
    }

    @Test
    fun `Test to get list games by passing a genre in DB`() {
        // Arrange
        val requestBody = """{"genre": "Action"}"""
        val request = Request(Method.GET, "/games/list").body(requestBody).query("genre", "Action")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/list"))
        // Act
        val response = webApi.listGames(routedRequest)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
        println("LIST  OF GAMES:" + response.bodyString())
    }

    @Test
    fun `Test to get list games by passing a developer in DB`() {
        // Arrange
        val requestBody = """{"dev": "EA"}"""
        val request = Request(Method.GET, "/games/list").body(requestBody).query("dev", "EA")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/list"))
        // Act
        val response = webApi.listGames(routedRequest)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
        println("LIST  OF GAMES:" + response.bodyString())
    }

    @Test
    fun `testing to get list games by passing a genre and a developer in DB`() {
        // Arrange
        val requestBody = """{"genre": "Action"}"""
        val request = Request(Method.GET, "/games/list/EA").body(requestBody).query("dev", "EA").query("genre", "Action")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/list/{dev}"))
        // Act
        val response = webApi.listGames(routedRequest)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
        println("LIST  OF GAMES:" + response.bodyString())
    }

}

 */