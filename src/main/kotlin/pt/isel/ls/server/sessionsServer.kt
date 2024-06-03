package pt.isel.ls.server

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.DELETE
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.singlePageApp
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory
import pt.isel.ls.server.api.*
import pt.isel.ls.server.data.DataBD
import pt.isel.ls.server.services.Services


fun main() {
    val webApi = WebApi(Services(DataBD()))

    val players =
        routes(
            "/players/list" bind GET to webApi::listPlayers,
            "/players/{id}" bind GET to webApi::getDetailsOfPlayer,
            "/players" bind POST to webApi::createPlayer,
            "/players/login/log" bind GET to webApi::login,
            "/players/sessions/{id}" bind GET to webApi::getSessionsOfPlayer,
        )

    val games =
        routes(
            "/games/list" bind GET to webApi::listGames,
            "/games/details/{id}" bind GET to webApi::detailsGame,
            "/games/createGame" bind POST to webApi::createNewGame,
            "/games/getGameByName/{name}" bind GET to webApi::getGameByName,
            "/games/listGames" bind GET to webApi::showGames,
            "/games/genres" bind GET to webApi::getGenres,
        )

    val sessions =
        routes(
            "/sessions/createSession" bind POST to webApi::createNewSession,
            "/sessions/details/{id}" bind GET to webApi::getSessionDetails,
            "/sessions/addPlayer" bind POST to webApi::addPLayerToSession,
            "/sessions/listSessions/{gid}" bind GET to webApi::getListSessions,
            "/sessions/update/{id}" bind POST to webApi::updateSession,
            "/sessions/delete/{id}" bind DELETE to webApi::deleteSession,
            "/sessions/removePlayer" bind DELETE to webApi::removePlayerFromSession,
            "/sessions/checkSession" bind POST to webApi::checkSession
        )
    val app =
        routes(
            sessions,
            games,
            players,
            singlePageApp(ResourceLoader.Directory("static-content"))
        )

    val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")
    val port = System.getenv("PORT")?.toInt() ?: 9001
    val jettyServer = app.asServer(Jetty(port)).start()

    logger.info("server started listening at port: ${jettyServer.port()}")
    //readln()
    //jettyServer.stop()

    //logger.info("leaving Main")
}



