package pt.isel.ls.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import pt.isel.ls.domain.*

@Serializable
data class SessionOutput(
    val id: UInt,
    val nofplayers: Int,
    val sessionDate: LocalDateTime, val game: GameOutput,
    val state: String,
    val associatedPlayers: MutableList<PlayerOutput>?
)

@Serializable
data class GameOutput(
    val gameId: UInt,
    val gameName: String,
    val gameDev: String,
    val gameGenre: Set<Genre>
)

@Serializable
data class PlayerOutput(
    val id: UInt,
    val name: String,
    val username: String,
    val email: String,
    val token: String
)





