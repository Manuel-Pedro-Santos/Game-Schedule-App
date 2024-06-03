package pt.isel.ls.server.services


import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.Genre
import pt.isel.ls.domain.Game
import pt.isel.ls.domain.PlayerDC
import pt.isel.ls.domain.Session
import pt.isel.ls.models.PlayerOutput
import pt.isel.ls.server.data.Data
import pt.isel.ls.utils.hashWithoutSalt
import java.util.*

class Services(val data: Data) {
    enum class TYPESTATE(val state: String) {
        OPEN("OPEN"), CLOSED("CLOSED")


    }

    fun createPlayer(name: String, username: String, email: String, password: String): Pair<UUID, Int> {
        val token = UUID.randomUUID()
        val p = data.getPlayerByEmail(email)
        if (p != null) throw IllegalStateException("This email ($email) is already in use")
        val u = data.getPlayerByUsername(username)
        if (u != null) throw IllegalStateException("This username ($username) is already in use")
        val id = data.createPlayer(name, username, email, password, token)

        return Pair(token, id)
    }

    fun login(username: String, password: String): Pair<String, Int> {
        val player = data.getPlayerByUsername(username) ?: throw IllegalArgumentException("Invalid username")
        if (player.password != hashWithoutSalt(password)) throw IllegalArgumentException("Invalid password")
        return Pair(player.token, player.id.toInt())
    }

    // Get the details of a player by id
    fun getPlayerById(id: UInt): PlayerDC? =
        data.getPlayerById(id)

    //Gets the list of players from a skip position with a limit of players to show
    fun getListOfPlayers(limit: Int, skip: Int): List<PlayerDC> =
        data.getListOfPlayers(limit, skip)

    fun createGame(name: String, developer: String, genres: Set<Genre>?): Int =
        data.createGame(name, developer, genres)

    fun getGameIdByName(name: String): Game? =
        data.getGameByName(name)

    fun showGames(limit: Int, skip: Int): List<Game> =
        data.selectAllGames(limit, skip)

    fun getGameDetails(id: UInt): Game? =
        data.getGameDetails(id)

    fun listGames(name: String, genres: Set<Genre>, developer: String, limit: Int, skip: Int): List<Game> =
        data.getGameList(name, genres, developer, limit, skip)

    fun createSession(playerID: UInt, capacity: UInt, gameName: String, date: LocalDateTime): Int {
        val game = data.getGameByName(gameName) ?: throw IllegalArgumentException("Game not found")
        return data.createSession(playerID, capacity, game.gameId, date)
    }

    fun addPlayerToSession(id: UInt, sID: UInt): Session {
        val (session, pout) = sessionPlayerOutput(sID, id)
        if (session?.associatedPlayers!!.contains(pout)) throw IllegalArgumentException("Player already in session")
        if (session.associatedPlayers.size == session.nofplayers) throw IllegalArgumentException("Session is full")
        return data.addPLayerToSession(id, sID)


    }

    fun getDetailsOfSession(id: UInt): Session? =
        data.getDetailsOfSession(id)

    fun getListSession(
        gid: UInt,
        date: LocalDateTime? = null,
        state: TYPESTATE? = null,
        nick: String? = null
    ): List<Session> {
        if (nick == null) {
            return data.getListSession(gid, date, state, nick)
        }
        val playerByName = data.getPlayerByUsername(nick)
        return data.getListSession(gid, date, state, playerByName?.id)
    }


    fun deleteSession(id: UInt): Boolean =
        data.deleteSession(id)

    fun updateSession(id: UInt, capacity: Int?, date: String?, state: String?): Session {
        if (capacity == null && date == null && state == null) throw IllegalArgumentException("No parameters to update")
        if(date != null){
            LocalDateTime.parse(date)
            if(state!= null){
                return data.updateSession(id, capacity?.toUInt(), LocalDateTime.parse(date), TYPESTATE.valueOf(state))
            }
            return data.updateSession(id, capacity?.toUInt(), LocalDateTime.parse(date), null)

        }
        else{
            if(state!= null){
                return data.updateSession(id, capacity?.toUInt(), null, TYPESTATE.valueOf(state))
            }
        }
        return data.updateSession(id, capacity?.toUInt(), null, null)

    }

    fun removePlayerFromSession(id: UInt, sID: UInt): Session {
        val (session, pout) = sessionPlayerOutput(sID, id)
        if (!session?.associatedPlayers!!.contains(pout)) throw IllegalArgumentException("Player not in session")
        return data.removePlayerFromSession(id, sID)
    }

    fun getplayertoken(bearerToken: String): String? {
        val token = bearerToken.split(" ")[1]
        return data.getPlayerToken(token)
    }



    fun getSessionsOfPlayer(id: UInt): List<Session> {
        if (data.getPlayerById(id) != null)
            return data.getSessionsOfPlayer(id)
        else
            throw IllegalArgumentException("Player not found")

    }

    fun getPlayerByUsername(username: String): PlayerDC? {
        return data.getPlayerByUsername(username)
    }

    fun checkSession(id: UInt, sID: UInt): Boolean {
        val (session, pout) = sessionPlayerOutput(sID, id)
        return session?.associatedPlayers!!.contains(pout)
    }

    private fun sessionPlayerOutput(
        sID: UInt,
        id: UInt
    ): Pair<Session?, PlayerOutput> {
        val session = data.getDetailsOfSession(sID)
        val player = data.getPlayerById(id)
        val pout = PlayerOutput(player!!.id, player.name, player.username, player.email, player.token)
        if (session == null) throw IllegalArgumentException("Session not found")
        return Pair(session, pout)
    }
}