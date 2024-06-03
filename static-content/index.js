import router from "./router.js";
import handlers from "./handlers/handlers.js";
import gamesHandler from "./handlers/gamesHandler.js";
import sessionsHandler from "./handlers/sessionsHandler.js";
import playersHandler from "./handlers/playersHandler.js";


window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler() {
    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("login", playersHandler.createPlayerHandler)
    router.addRouteHandler("signup", playersHandler.signUpPlayerHandler)
    router.addRouteHandler("logout", playersHandler.logoutHandler)
    router.addRouteHandler("players", playersHandler.getPlayersHome)
    router.addRouteHandler("players/list", playersHandler.getListPlayers)
    router.addRouteHandler("players/{id}", playersHandler.getPlayerDetails)
    router.addRouteHandler("games", gamesHandler.getGamesHome)
    router.addRouteHandler("games/createGame", gamesHandler.createGameHandler)
    router.addRouteHandler("games/list", gamesHandler.getGamesList)
    router.addRouteHandler("games/details/{gameId}", gamesHandler.getGamesDetails)
    router.addRouteHandler("session", sessionsHandler.getSessionsHome)
    router.addRouteHandler("sessions/details/{id}", sessionsHandler.getSessionDetails)
    router.addRouteHandler("sessions/listSessions/{gid}", sessionsHandler.getListSessions)
    router.addRouteHandler("sessions/createSession", sessionsHandler.createSessionHandler)
    router.addRouteHandler("sessions/addPlayerToSession/{sid}", sessionsHandler.addPlayerToSession)
    router.addRouteHandler("sessions/delete/{sid}", sessionsHandler.deleteSession)
    router.addRouteHandler("sessions/update/{sid}", sessionsHandler.updateSession)
    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")

    hashChangeHandler()
}

function hashChangeHandler() {

    const mainContent = document.getElementById("mainContent")
    const path = window.location.hash.replace("#", "")
    const {handler, params} = router.getRouteHandlerWithParams(path)
    handler(mainContent, params)
}



