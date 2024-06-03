package pt.isel.ls.http.dataDB

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.server.api.WebApi
import pt.isel.ls.server.data.DataBD
import pt.isel.ls.server.services.Services
import kotlin.test.Test
/*
class SessionHttpBDTests {

    private val webApi = WebApi(Services(DataBD()))
    /*
    @Test
    fun `Create a new session in DB`() {
        // Arrange
        val request = Request(Method.POST, "/sessions/createSession")
            .body("""{
                    "playerID":1,
                    "capacity": 2,
                    "gid": 1,
                    "date": "2024-03-08T20:03:03"
                }""")
        // Act
        val response = { _: Request -> webApi.createNewSession(request) }
        val ret = response(request)
        // Assert
        assert(ret.status == Status.CREATED)
        println("CREATED: ${ret.bodyString()}")
    }
    */
    @Test
    fun `Test to get session details by passing an id in DB`() {
        val id = "1"
        // Arrange
        val request = Request(Method.GET, "/sessions/$id")
        // Act
        val response = webApi.getSessionDetails(request)
        // Assert

        assert(response.header("content-type") == "application/json")
        println("DETAILS OF SESSION $id:" + response.bodyString())

    }
}

*/