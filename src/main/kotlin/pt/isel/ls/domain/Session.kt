package pt.isel.ls.domain


import kotlinx.datetime.LocalDateTime
import pt.isel.ls.models.PlayerOutput
import pt.isel.ls.server.services.Services


data class Session(
    val id: UInt,
    var nofplayers: Int,
    var sessionDate: LocalDateTime,
    val game: Game,
    val state: Services.TYPESTATE = Services.TYPESTATE.OPEN,
    val associatedPlayers: MutableList<PlayerOutput>?
)







