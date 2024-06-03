package pt.isel.ls.server.data

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.domain.*
import pt.isel.ls.models.PlayerOutput
import pt.isel.ls.server.services.Services
import pt.isel.ls.utils.hashWithoutSalt
import java.util.*
import kotlin.NoSuchElementException

val listSessions = mutableListOf(
    Session(
        1u,
        2,
        LocalDateTime(2024, 5, 1, 12, 12, 12),
        game = Game(1u, "game1", gameDev = "Ricardo", gameGenre = setOf(Genre.ACTION)),
        state = Services.TYPESTATE.OPEN,
        associatedPlayers = mutableListOf(
            PlayerOutput(1u, "Gonçalo", "goncalo", "teste@gmail.com", "c04c8d06-a64b-494c-b49b-2a484cdbc57a")
        )
    ),
    Session(
        2u,
        4,
        LocalDateTime(2024, 5, 1, 12, 12, 12),
        game = Game(2u, "game2", gameDev = "Manuel", gameGenre = setOf(Genre.ACTION)),
        Services.TYPESTATE.OPEN,
        associatedPlayers = mutableListOf(
            PlayerOutput(1u, "Gonçalo", "goncalo", "teste@gmail.com", "c04c8d06-a64b-494c-b49b-2a484cdbc57a"),
            PlayerOutput(2u, "Manuel", "manuel", "manuel@gmail.com", "c04c8d06-a64b-494c-b49b-2a484cdbc57a")
        )
    ),
    Session(
        3u,
        5,
        LocalDateTime(2024, 5, 1, 12, 12, 12),
        game = Game(3u, "game3", gameDev = "Goncalo", gameGenre = setOf(Genre.ACTION)),
        Services.TYPESTATE.OPEN,
        associatedPlayers = mutableListOf(
            PlayerOutput(1u, "Teste1", "teste1", "teste1@gmail.com", "c04c8d06-a64b-494c-b49b-2a484cdbc57a")
        )
    ),
    Session(
        4u,
        8,
        LocalDateTime(2024, 5, 1, 12, 12, 12),
        game = Game(4u, "game4", gameDev = "Isel", gameGenre = setOf(Genre.ACTION)),
        associatedPlayers = mutableListOf(
            PlayerOutput(1u, "Gonçalo", "goncalo", "teste@gmail.com", "c04c8d06-a64b-494c-b49b-2a484cdbc57a"),
        )
    ),
    Session(
        5u,
        2,
        LocalDateTime(2023, 5, 1, 12, 12, 12),
        game = Game(4u, "game4", gameDev = "Test4", gameGenre = setOf(Genre.ACTION)),
        associatedPlayers = mutableListOf(
            PlayerOutput(1u, "test2", "test2", "teste2@gmail.com", "c04c8d06-a64b-494c-b49b-2a484cdbc57a")
        )
    ),


    Session(
        6u,
        8,
        LocalDateTime(2024, 5, 1, 12, 12, 12),
        game = Game(1u, "game1", gameDev = "Isel", gameGenre = setOf(Genre.ACTION)),
        associatedPlayers = mutableListOf()
    )

)
val playersList = mutableListOf(
    PlayerDC(1u, "Gonçalo", "goncalo", "goncalo@gmail.com", "isel", "4d259054-dbfc-4f5d-945e-b5c33b89a2c5"),
    PlayerDC(2u, "Manuel", "manuel", "2003rfco@gmail.com", "isel", UUID.randomUUID().toString()),
    PlayerDC(3u, "Ricardo", "ricardo", "2003rfco@gmail.com", "isel", UUID.randomUUID().toString()),
    PlayerDC(4u, "OLA", "ola", "2003rfco@gmail.com", "isel", UUID.randomUUID().toString()),
    PlayerDC(5u, "teste", "teste", "2003rfco@gmail.com", "isel", UUID.randomUUID().toString()),
    PlayerDC(6u, "scp", "scp", "2003rfco@gmail.com", "isel", UUID.randomUUID().toString()),
    PlayerDC(7u, "SLB", "slb", "2003rfco@gmail.com", "isel", UUID.randomUUID().toString()),
    PlayerDC(8u, "FCP   ", "fcp", "2003rfco@gmail.com", "isel", UUID.randomUUID().toString()),
)
val gameList = mutableListOf(
    Game(1u, "game1", "Test1", setOf(Genre.ACTION)),
    Game(2u, "game2", "Test1", setOf(Genre.ACTION)),
    Game(3u, "game3", "Test1", setOf(Genre.ACTION)),
    Game(4u, "game4", "Test4", setOf(Genre.ACTION)),
    Game(5u, "game5", "Test5", setOf(Genre.ACTION)),
    Game(6u, "game6", "Test1", setOf(Genre.ACTION, Genre.ADVENTURE)),
    Game(7u, "game7", "Test1", setOf(Genre.ACTION, Genre.ADVENTURE)),
) // size = 4


class DataMem : Data {
    override fun createGame(name: String, developer: String, genres: Set<Genre>?): Int {
        if (genres == null) throw IllegalArgumentException("Genres cannot be null")
        val game = Game((gameList.size + 1).toUInt(), name, developer, genres)
        if (gameList.any { it.gameName == game.gameName }) throw IllegalArgumentException("Game with \"${name}\" already exists")
        gameList.add(game)
        return game.gameId.toInt()
    }

    override fun getGameByName(name: String): Game? {
        return gameList.find { it.gameName == name }
    }

    override fun getGameDetails(id: UInt): Game? {
        return gameList.find { it.gameId == id }
    }

    //Returns a list of games with a specific genre and developer and skiping a number of games and limiting the number of games
    override fun getGameList(name: String, genres: Set<Genre>, developer: String, limit: Int, skip: Int): List<Game> {
        // Filter the gameList based on the specified genre and developer
        val filteredList = gameList.filter {
            (it.gameGenre == genres || genres.isEmpty()) && (developer.isBlank() || it.gameDev == developer)
        }

        // Apply skipping
        val skippedList = filteredList.drop(skip)

        // Check if the limit is greater than the size of the filtered list
        val limitedList = if (limit > skippedList.size) {
            skippedList
        } else {
            skippedList.take(limit)
        }

        return limitedList
    }


    override fun getAllThePlayers(): List<PlayerDC> {
        return playersList
    }


    override fun createSession(playerID: UInt, capacity: UInt, gid: UInt, date: LocalDateTime): Int {
        listSessions.add(
            Session(
                listSessions.size.toUInt() + 1u,
                capacity.toInt(),
                date,
                game = gameList.find { it.gameId == gid } ?: throw IllegalArgumentException("Game not found"),
                associatedPlayers = mutableListOf(
                    PlayerOutput(
                        playerID,
                        "Teste",
                        "$playerID",
                        "$playerID@email.com",
                        "c04c8d06-a64b-494c-b49b-2a484cdbc57a"
                    )
                )
            )
        )
        return listSessions.size

    }

    override fun addPLayerToSession(id: UInt, sid: UInt): Session {
        val session = listSessions.firstOrNull { it.associatedPlayers!!.size < it.nofplayers }
            ?: throw IllegalArgumentException("No session available")
        session.associatedPlayers?.add(
            PlayerOutput(
                id,
                "Teste",
                "$id",
                "$id@gmail.com",
                "c04c8d06-a64b-494c-b49b-2a484cdbc57a"
            )
        )
        return session

    }

    override fun getDetailsOfSession(id: UInt): Session {
        return listSessions.find { it.id == id } ?: throw NoSuchElementException("Session not found")
    }

    override fun getListSession(
        gid: UInt,
        date: LocalDateTime?,
        state: Services.TYPESTATE?,
        pid: UInt?
    ): List<Session> {
        return listSessions.filter { it ->
            (it.game.gameId == gid) &&
                    (date == null || it.sessionDate == date) &&
                    (state == null || it.associatedPlayers!!.size < it.nofplayers) &&
                    (pid == null || it.associatedPlayers?.find { it.id == pid } != null)
        }
    }


    override fun createPlayer(name: String, username: String, email: String, password: String, token: UUID): Int {
        val id = (playersList.size + 1)
        val player = PlayerDC(id.toUInt(), name, username, email, hashWithoutSalt(password), token.toString())
        playersList.add(element = player)
        return id
    }

    override fun login(username: String, password: String): Int {
        val player = playersList.find { it.username == username && it.password == hashWithoutSalt(password) }
            ?: throw IllegalArgumentException("Invalid username or password")
        return player.id.toInt()
    }

    override fun getPlayerById(id: UInt): PlayerDC {
        return playersList.find { it.id == id } ?: throw NoSuchElementException("Player not found")
    }

    override fun getListOfPlayers(limit: Int, skip: Int): List<PlayerDC> {
        if (skip > playersList.size) throw NoSuchElementException("No more players")
        if (skip + limit > playersList.size) return playersList.subList(skip, playersList.size)

        return playersList.subList(skip, skip + limit)
    }

    override fun selectAllGames(limit: Int, skip: Int): List<Game> {
        if (skip > gameList.size) throw NoSuchElementException("No more games")
        if (skip + limit > gameList.size) return gameList.subList(skip, gameList.size)
        return gameList.subList(skip, skip + limit)
    }


    override fun getPlayerToken(token: String): String? {
        val player = playersList.find { it.token == token }
        if (player == null || player.token != token) return null
        return player.token

    }

    override fun getPlayerByEmail(email: String): PlayerDC? {
        return playersList.find { it.email == email }
    }

    override fun getPlayerByUsername(username: String): PlayerDC? {
        return playersList.find { it.username == username }
    }

    override fun deleteSession(sid: UInt): Boolean {
        val session = listSessions.find { it.id == sid } ?: throw NoSuchElementException("Session not found")
        return listSessions.remove(session)
    }

    override fun updateSession(sid: UInt, capacity: UInt?, date: LocalDateTime?, state: Services.TYPESTATE?): Session {
        val session = listSessions.find { it.id == sid } ?: throw NoSuchElementException("Session not found")
        if (capacity != null) session.nofplayers = capacity.toInt()
        if (date != null) session.sessionDate = date
        return session
    }

    override fun removePlayerFromSession(id: UInt, sID: UInt): Session {
        val player = playersList.find { it.id == id } ?: throw NoSuchElementException("Player not found")

        val session = listSessions.find { it.id == sID } ?: throw NoSuchElementException("Session not found")
        session.associatedPlayers?.remove(
            PlayerOutput(
                player.id,
                player.name,
                player.username,
                player.email,
                "c04c8d06-a64b-494c-b49b-2a484cdbc57a"
            )
        )
        return session
    }

    override fun getSessionsOfPlayer(id: UInt): List<Session> {
        val player = playersList.find { it.id == id } ?: throw NoSuchElementException("Player not found")
        val allSessions = listSessions.filter {
            it.associatedPlayers?.contains(
                PlayerOutput(
                    player.id,
                    player.name,
                    player.username,
                    player.email,
                    "c04c8d06-a64b-494c-b49b-2a484cdbc57a"
                )
            ) == true
        }
        return allSessions
    }

    override fun getGameListByName(name: String): List<Game> {
        return gameList.filter { it.gameName == name }
    }

}




