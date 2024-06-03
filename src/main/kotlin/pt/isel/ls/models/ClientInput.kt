package pt.isel.ls.models

import kotlinx.serialization.Serializable


@Serializable
data class SessionRequestClient(val playerID: UInt, val capacity: Int, val gameName: String, val date: String)

@Serializable
data class AddPlayerID(val id: Int, val sessionId: Int)

@Serializable
data class SessionUpdate(val capacity: Int?, val date: String?, val state: String?)

@Serializable
data class PlayerID(
    val name: String, val username: String, val email: String, val password: String
)


@Serializable
data class GameDataHandler(
    val gameName: String, val gameDev: String, val gameGenre: String
)



