package pt.isel.ls.server.api

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path
import pt.isel.ls.domain.Genre
import pt.isel.ls.domain.PlayerDC
import pt.isel.ls.domain.Session
import pt.isel.ls.domain.classifyGenre
import pt.isel.ls.models.*
import pt.isel.ls.server.services.Services
import pt.isel.ls.utils.pageSessions

class WebApi(val services: Services) {
    fun listPlayers(request: Request) = tryRun {
        val limit = request.query("limit")?.toInt() ?: DEFAULT_MAX_LIMIT
        val skip = request.query("skip")?.toInt() ?: DEFAULT_MIN_LIMIT
        val playersSubSequence =
            services.getListOfPlayers(limit, skip).map { PlayerOutput(it.id, it.name, it.username, it.email, it.token) }
        Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(playersSubSequence))
    }

    fun getDetailsOfPlayer(request: Request) = tryRun {
        val id = request.path("id")?.toUInt() ?: 0u
        val player = services.getPlayerById(id)?.let { PlayerOutput(it.id, it.username, it.name, it.email, it.token) }
        Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(player))
    }

    fun checkSession(request: Request) = tryRun {
        val req = Json.decodeFromString<AddPlayerID>(request.bodyString())
        val check = services.checkSession(req.id.toUInt(), req.sessionId.toUInt())
        Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(check))
    }

    fun createPlayer(request: Request) = tryRun {
        val player = Json.decodeFromString<PlayerID>(request.bodyString())
        val playerExists: PlayerDC? = services.getPlayerByUsername(player.username)
        if (playerExists == null) {
            val (token, id) = services.createPlayer(player.name, player.username, player.email, player.password)
            Response(Status.CREATED).header("content-type", "application/json").body(
                Json.encodeToString(
                    PlayerOutput(
                        id.toUInt(), player.name, player.username, player.email, token.toString()
                    )
                )
            )
        } else {
            Response(Status.BAD_REQUEST).header("content-type", "application/json")
                .body(Json.encodeToString("Player already exists"))
        }
    }

    fun login(request: Request) = tryRun {
        val username = request.query("username") ?: ""
        val password = request.query("password") ?: ""
        val (token, id) = services.login(username = username, password = password)
        Response(Status.OK).header("content-type", "application/json").body(
            Json.encodeToString(
                PlayerOutput(
                    id = id.toUInt(),
                    name = services.getPlayerById(id.toUInt())?.name.toString(),
                    username = username,
                    email = services.getPlayerById(id.toUInt())?.email ?: "",
                    token = token
                )
            )
        )
    }

    fun getSessionsOfPlayer(request: Request) = tryRun {
        val id = request.path("id")?.toUInt() ?: 0u
        val sessions = services.getSessionsOfPlayer(id)
        responseSessionsOutput(sessions)
    }

    private fun responseSessionsOutput(sessions: List<Session>): Response {
        val sessionsOutput = sessions.map {
            SessionOutput(
                it.id,
                it.nofplayers,
                it.sessionDate,
                GameOutput(it.game.gameId, it.game.gameName, it.game.gameDev, it.game.gameGenre),
                it.state.toString(),
                it.associatedPlayers
            )
        }
        return Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(sessionsOutput))
    }

    fun listGames(request: Request) = tryRun {
        val name = request.query("gameName") ?: ""
        val limit = request.query("limit")?.toInt() ?: DEFAULT_MAX_LIMIT
        val skip = request.query("skip")?.toInt() ?: DEFAULT_MIN_LIMIT
        val genresString = request.query("genre") ?: ""
        val genres =
            if (genresString.isBlank()) emptySet() else genresString.split(",").map { classifyGenre(it.trim()) }.toSet()
        val developer = request.query("dev") ?: ""
        val games = services.listGames(name, genres, developer, limit, skip)
        val outputGames = games.map { GameOutput(it.gameId, it.gameName, it.gameDev, it.gameGenre) }
        Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(outputGames))
    }

    fun detailsGame(request: Request) = tryRun {
        val id = request.path("id")?.toUInt()
        val game = id?.let { services.getGameDetails(id) }
        val outputGame = game?.let { GameOutput(it.gameId, it.gameName, it.gameDev, it.gameGenre) }
        if (game != null) {
            Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(outputGame))
        } else {
            Response(Status.NOT_FOUND).header("content-type", "application/json")
                .body(Json.encodeToString("Game not found"))
        }
    }

    fun createNewGame(request: Request) = tryRun {
        val gameData = Json.decodeFromString<GameDataHandler>(request.bodyString())
        val newGenre = gameData.gameGenre.split(",").map { classifyGenre(it) }.toSet()
        val newGame = services.createGame(gameData.gameName, gameData.gameDev, newGenre)
        val gameOutput = GameOutput(newGame.toUInt(), gameData.gameName, gameData.gameDev, newGenre)
        Response(Status.CREATED).header("content-type", "application/json").body(Json.encodeToString(gameOutput))
    }

    fun getGameByName(request: Request) = tryRun {
        val name = request.path("name") ?: ""
        val game = services.getGameIdByName(name)
        val outputGame = game?.let { GameOutput(it.gameId, it.gameName, it.gameDev, it.gameGenre) }
        Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(outputGame?.gameId))
    }

    fun showGames(request: Request) = tryRun {
        request.header("Authorization")?.let {
            if (services.getplayertoken(it) == null) Response(Status.OK).header("content-type", "application/json")
                .body("Invalid User Token")
        }
        val games = services.showGames(20, DEFAULT_MIN_LIMIT)
        val hasMapToReturn = mutableMapOf<UInt, String>()
        games.forEach {
            hasMapToReturn[it.gameId] = it.gameName
        }
        Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(hasMapToReturn))
    }

    fun getGenres(request: Request) = tryRun {
        request.bodyString()
        val genres: List<String> = Genre.entries.map { it.identifier }
        Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(genres))
    }

    fun createNewSession(request: Request) = tryRun {
        val session = Json.decodeFromString<SessionRequestClient>(request.bodyString())
        if (services.getplayertoken(
                request.header("Authorization").toString()
            ) == null
        ) Response(Status.BAD_REQUEST).header("content-type", "application/json").body("Invalid User Token")
        val newSession = services.createSession(
            session.playerID, session.capacity.toUInt(), session.gameName, LocalDateTime.parse(session.date)
        )
        Response(Status.CREATED).header("content-type", "application/json").body(Json.encodeToString(newSession))
    }

    fun getSessionDetails(request: Request) = tryRun {
        val id = request.path("id")?.toUInt() ?: 0u
        val session = services.getDetailsOfSession(id)
        val outputSession = session?.let {
            SessionOutput(
                it.id,
                it.nofplayers,
                it.sessionDate,
                GameOutput(it.game.gameId, it.game.gameName, it.game.gameDev, it.game.gameGenre),
                it.state.toString(),
                it.associatedPlayers
            )
        }
        Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(outputSession))
    }

    fun addPLayerToSession(request: Request) = tryRun {
        if (services.getplayertoken(
                request.header("Authorization").toString()
            ) == null
        ) Response(Status.OK).header("content-type", "application/json").body("Invalid User Token")
        val playerID = Json.decodeFromString<AddPlayerID>(request.bodyString())
        val session = services.addPlayerToSession(playerID.id.toUInt(), playerID.sessionId.toUInt())
        createSessionOutput(session)

    }

    fun getListSessions(request: Request) = tryRun {
        val gid = request.path("gid")?.toUInt() ?: 0u
        val limit = request.query("limit")?.toInt() ?: DEFAULT_MAX_LIMIT
        val skip = request.query("skip")?.toInt() ?: DEFAULT_MIN_LIMIT
        val stateQuery = request.query("state") ?: ""
        val stateDate = request.query("date") ?: ""
        val date: LocalDateTime? = if (stateDate.isNotBlank()) LocalDateTime.parse(stateDate) else null
        val state: Services.TYPESTATE? = if (stateQuery.isNotBlank()) Services.TYPESTATE.valueOf(stateQuery) else null
        val playerName = request.query("nick")
        val sessions = services.getListSession(gid, date, state, playerName)
        val sessionsList = pageSessions(sessions, limit, skip)
        val session = sessionsList.map {
            SessionOutput(
                it.id,
                it.nofplayers,
                it.sessionDate,
                GameOutput(it.game.gameId, it.game.gameName, it.game.gameDev, it.game.gameGenre),
                it.state.toString(),
                it.associatedPlayers
            )
        }
        Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(session))
    }

    fun deleteSession(request: Request) = tryRun {
        if (services.getplayertoken(
                request.header("Authorization").toString()
            ) == null
        ) Response(Status.OK).header("content-type", "application/json")
            .body("Invalid User Token or not in session to delete")
        val id = request.path("id")?.toUInt() ?: 0u
        services.deleteSession(id)
        Response(Status.OK).header("content-type", "application/json").body("Session deleted")
    }

    fun updateSession(request: Request) = tryRun {
        if (services.getplayertoken(
                request.header("Authorization").toString()
            ) == null
        ) Response(Status.OK).header("content-type", "application/json")
            .body("Invalid User Token or not in session to update")
        val id = request.path("id")?.toUInt() ?: 0u
        val session = Json.decodeFromString<SessionUpdate>(request.bodyString())
        val newSession = services.updateSession(id, session.capacity, session.date, session.state)
        createSessionOutput(newSession)
    }

    fun removePlayerFromSession(request: Request) = tryRun {
        if (services.getplayertoken(
                request.header("Authorization").toString()
            ) == null
        ) Response(Status.OK).header("content-type", "application/json")
            .body("Invalid User Token or not in session to remove")
        val playerID = Json.decodeFromString<AddPlayerID>(request.bodyString())
        val session = services.removePlayerFromSession(playerID.id.toUInt(), playerID.sessionId.toUInt())
        createSessionOutput(session)
    }

    private fun createSessionOutput(session: Session): Response {
        val sessionOutput = SessionOutput(
            session.id,
            session.nofplayers,
            session.sessionDate,
            GameOutput(session.game.gameId, session.game.gameDev, session.game.gameDev, session.game.gameGenre),
            session.state.toString(),
            session.associatedPlayers
        )
        return Response(Status.OK).header("content-type", "application/json").body(Json.encodeToString(sessionOutput))
    }
}




