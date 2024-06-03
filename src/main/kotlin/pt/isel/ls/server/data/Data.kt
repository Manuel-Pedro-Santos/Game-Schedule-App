package pt.isel.ls.server.data

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Genre
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.PlayerDC
import pt.isel.ls.domain.Session
import pt.isel.ls.server.services.Services.TYPESTATE
import java.util.UUID

interface Data {

    fun createGame(name: String, developer: String, genres: Set<Genre>?): Int

    fun getGameByName(name: String): Game?
    fun getGameDetails(id: UInt): Game?
    fun getGameList(name: String, genres: Set<Genre>, developer: String, limit: Int, skip: Int): List<Game>

    fun createPlayer(name: String, username: String, email: String, password: String, token: UUID): Int
    fun login(username: String, password: String): Int
    fun getPlayerById(id: UInt): PlayerDC?
    fun getListOfPlayers(limit: Int, skip: Int): List<PlayerDC>

    fun selectAllGames(limit: Int, skip: Int): List<Game>
    fun createSession(playerID: UInt, capacity: UInt, gid: UInt, date: LocalDateTime): Int
    fun getDetailsOfSession(id: UInt): Session?
    fun getListSession(gid: UInt, date: LocalDateTime?, state: TYPESTATE?, pid: UInt?): List<Session>
    fun addPLayerToSession(id: UInt, sid: UInt): Session
    fun getPlayerToken(token: String): String?
    fun getPlayerByEmail(email: String): PlayerDC?

    fun deleteSession(sid: UInt): Boolean
    fun updateSession(sid: UInt, capacity: UInt?, date: LocalDateTime?, state: TYPESTATE?): Session
    fun removePlayerFromSession(id: UInt, sID: UInt): Session
    fun getSessionsOfPlayer(id: UInt): List<Session>
    fun getGameListByName(name: String): List<Game>
    fun getAllThePlayers(): List<PlayerDC>
    fun getPlayerByUsername(username: String): PlayerDC?
}




