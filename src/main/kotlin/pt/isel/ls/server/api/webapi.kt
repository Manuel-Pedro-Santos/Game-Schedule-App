package pt.isel.ls.server.api

import org.http4k.core.Response
import org.http4k.core.Status

const val DEFAULT_MAX_LIMIT = 10
const val DEFAULT_MIN_LIMIT = 0

fun tryRun(block: () -> Response): Response {
    return try {
        return block()
    } catch (e: IllegalStateException) {
        Response(Status.BAD_REQUEST).header("content-type", "application/json")
            .body("Invalid argument provided: " + e.message)
    } catch (e: IllegalArgumentException) {
        println(e.message)
        Response(Status.BAD_REQUEST).header("content-type", "application/json")
            .body("Invalid argument provided: " + e.message)
    } catch (e: NoSuchElementException) {
        Response(Status.NOT_FOUND).header("content-type", "application/json").body("Element not found: " + e.message)
    } catch (e: Exception) {
        println(e.message)
        Response(Status.INTERNAL_SERVER_ERROR).header("content-type", "application/json")
            .body("Internal server error occurred: " + e.message)
    }
}






