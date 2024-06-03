package pt.isel.ls.http.dataMem



import org.http4k.core.*
import org.http4k.routing.RoutedRequest
import pt.isel.ls.server.api.*
import pt.isel.ls.server.data.DataMem
import pt.isel.ls.server.services.Services
import kotlin.test.Test

@Suppress("NAME_SHADOWING")
class GameRequestsTest {
    private val webApi = WebApi(Services(DataMem()))
    
    @Test
    fun `Create a new game`() {
        // Arrange
        val request = Request(Method.POST, "/games/createGame")
            .body("""{"gameName": "FIFA 22", "gameDev": "EA", "gameGenre": "Sports"}""")
        // Act
        val response = { request: Request -> webApi.createNewGame(request) }
        val ret = response(request)
        // Assert
        assert(ret.status == Status.CREATED)
        assert(ret.header("content-type") == "application/json")
    }
    @Test
    fun `Test to get game details by passing an id`() {
        // Arrange
        val request = Request(Method.GET, "/games/details/1")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/details/{id}"))
        // Act
        val response = webApi.detailsGame(routedRequest)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")

    }

    @Test
    fun `Test to get list games by passing a genre and a developer`() {
        // Arrange
        val requestBody = """{"genre": "Action"}"""
        val request = Request(Method.GET, "/games/list/Test1").body(requestBody).query("limit", "2")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/list/{dev}"))
        // Act
        val response = webApi.listGames(routedRequest)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
    }

    @Test
fun `Test to get list games by passing a genre`() {
        // Arrange
        val requestBody = """{"genre": "Action"}"""
        val request = Request(Method.GET, "/games/list").body(requestBody).query("limit", "2")
        // Act
        val response = webApi.listGames(request)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
    }

    @Test
    fun `Test to get list games by passing a developer`() {
        // Arrange
        val requestBody = """{"dev": "Test1"}"""
        val request = Request(Method.GET, "/games/list").body(requestBody).query("limit", "2")
        // Act
        val response = webApi.listGames(request)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
    }

    @Test
    fun `Test to get list games by passing a developer and a genre`() {
        // Arrange
        val requestBody = """{"dev": "Test1", "genre": "Action"}"""
        val request = Request(Method.GET, "/games/list").body(requestBody).query("limit", "2")
        // Act
        val response = webApi.listGames(request)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
    }

    @Test
    fun `Test to get list games by passing a name`() {
        // Arrange
        val requestBody = """{"name": "FIFA 22"}"""
        val request = Request(Method.GET, "/games/list").body(requestBody).query("limit", "2")
        // Act
        val response = webApi.listGames(request)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
    }

    @Test
    fun `Test to get list games by passing a name and a genre`() {
        // Arrange
        val requestBody = """{"name": "FIFA 22", "genre": "Sports"}"""
        val request = Request(Method.GET, "/games/list").body(requestBody).query("limit", "2")
        // Act
        val response = webApi.listGames(request)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
    }

    @Test
    fun `Test to get list games by passing a name and a developer`() {
        // Arrange
        val requestBody = """{"name": "FIFA 22", "dev": "EA"}"""
        val request = Request(Method.GET, "/games/list").body(requestBody).query("limit", "2")
        // Act
        val response = webApi.listGames(request)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
    }

    @Test
    fun `Test to get list games by passing a name, a developer and a genre`() {
        // Arrange
        val requestBody = """{"name": "FIFA 22", "dev": "EA", "genre": "Sports"}"""
        val request = Request(Method.GET, "/games/list").body(requestBody).query("limit", "2")
        // Act
        val response = webApi.listGames(request)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("content-type") == "application/json")
    }
}