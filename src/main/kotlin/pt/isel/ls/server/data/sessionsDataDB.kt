package pt.isel.ls.server.data

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.*
import pt.isel.ls.models.PlayerOutput
import pt.isel.ls.server.services.Services
import pt.isel.ls.utils.hashWithoutSalt
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.util.UUID


class DataBD : Data {

    private val dataSource: PGSimpleDataSource = PGSimpleDataSource()
    private val jdbcDatabaseURL: String = System.getenv("JDBC_DATABASE_URL")

    init {
        dataSource.setURL(jdbcDatabaseURL)
    }

    override fun createGame(name: String, developer: String, genres: Set<Genre>?): Int {
        dataSource.connection.use { connection ->
            // Check if a game with the same name already exists
            connection.prepareStatement("SELECT * FROM game WHERE gname = ?").use { stmt ->
                stmt.setString(1, name)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    throw IllegalArgumentException("A game with the name $name already exists.")
                }
            }

            // If no game with the same name exists, proceed to create the new game
            connection.prepareStatement("INSERT INTO game(gname, gdeveloper, ggenre) VALUES (?, ?, ?) returning gid")
                .use { stmt ->
                    stmt.setString(1, name)
                    stmt.setString(2, developer)
                    val genreIds = genres?.map { it.ordinal }?.toTypedArray()
                    stmt.setArray(3, connection.createArrayOf("INTEGER", genreIds))
                    val rs = stmt.executeQuery()
                    if (rs.next())
                        return rs.getInt("gid")
                }
        }
        return -1
    }

    override fun getGameByName(name: String): Game? {
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT * FROM game WHERE gname = ?").use { stmt ->
                stmt.setString(1, name)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return buildGame(rs)
                }
            }
        }
        return null
    }


    override fun getGameDetails(id: UInt): Game? {
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT * FROM game WHERE gid = ?").use { stmt ->
                stmt.setInt(1, id.toInt())
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return buildGame(rs)
                }
            }
        }
        return null
    }

    override fun getGameList(name: String, genres: Set<Genre>, developer: String, limit: Int, skip: Int): List<Game> {
        val list = mutableListOf<Game>()
        dataSource.connection.use { connection ->
            val query = when {
                name.isNotBlank() && genres.isNotEmpty() && developer.isNotBlank() -> {
                    "SELECT * FROM game WHERE gname LIKE ? AND ggenre && ? AND gdeveloper = ? LIMIT ? OFFSET ?"
                }

                name.isNotBlank() && genres.isNotEmpty() -> {
                    "SELECT * FROM game WHERE gname LIKE ? AND ggenre && ? LIMIT ? OFFSET ?"
                }

                name.isNotBlank() && developer.isNotBlank() -> {
                    "SELECT * FROM game WHERE gname LIKE ? AND gdeveloper = ? LIMIT ? OFFSET ?"
                }

                genres.isNotEmpty() && developer.isNotBlank() -> {
                    "SELECT * FROM game WHERE ggenre && ? AND gdeveloper = ? LIMIT ? OFFSET ?"
                }

                genres.isNotEmpty() -> {
                    "SELECT * FROM game WHERE ggenre && ? LIMIT ? OFFSET ?"
                }

                developer.isNotBlank() -> {
                    "SELECT * FROM game WHERE gdeveloper = ? LIMIT ? OFFSET ?"
                }

                else -> {
                    "SELECT * FROM game WHERE gname LIKE ? LIMIT ? OFFSET ?"
                }
            }

            connection.prepareStatement(query).use { stmt ->
                when {
                    name.isNotBlank() && genres.isNotEmpty() && developer.isNotBlank() -> {
                        stmt.setString(1, "$name%")
                        val genreArray = connection.createArrayOf("INTEGER", genres.map { it.ordinal }.toTypedArray())
                        stmt.setArray(2, genreArray)
                        stmt.setString(3, developer)
                        stmt.setInt(4, limit)
                        stmt.setInt(5, skip)
                    }

                    name.isNotBlank() && genres.isNotEmpty() -> {
                        stmt.setString(1, "$name%")
                        val genreArray = connection.createArrayOf("INTEGER", genres.map { it.ordinal }.toTypedArray())
                        stmt.setArray(2, genreArray)
                        stmt.setInt(3, limit)
                        stmt.setInt(4, skip)
                    }

                    name.isNotBlank() && developer.isNotBlank() -> {
                        stmt.setString(1, "$name%")
                        stmt.setString(2, developer)
                        stmt.setInt(3, limit)
                        stmt.setInt(4, skip)
                    }

                    genres.isNotEmpty() && developer.isNotBlank() -> {
                        val genreArray = connection.createArrayOf("INTEGER", genres.map { it.ordinal }.toTypedArray())
                        stmt.setArray(1, genreArray)
                        stmt.setString(2, developer)
                        stmt.setInt(3, limit)
                        stmt.setInt(4, skip)
                    }

                    genres.isNotEmpty() -> {
                        val genreArray = connection.createArrayOf("INTEGER", genres.map { it.ordinal }.toTypedArray())
                        stmt.setArray(1, genreArray)
                        stmt.setInt(2, limit)
                        stmt.setInt(3, skip)
                    }

                    developer.isNotBlank() -> {
                        stmt.setString(1, developer)
                        stmt.setInt(2, limit)
                        stmt.setInt(3, skip)
                    }

                    else -> {
                        stmt.setString(1, "$name%")
                        stmt.setInt(2, limit)
                        stmt.setInt(3, skip)
                    }
                }

                getPlayers(stmt, list)
            }
        }
        return list
    }

    override fun getAllThePlayers(): List<PlayerDC> {
        dataSource.connection.use { connection ->
            val query = "SELECT * FROM player"
            connection.prepareStatement(query).use { stmt ->
                val rs = stmt.executeQuery()
                val list = mutableListOf<PlayerDC>()
                return getPLayers(rs, list)
            }
        }
    }


    override fun createPlayer(name: String, username: String, email: String, password: String, token: UUID): Int {
        dataSource.connection.use { connection ->
            connection.prepareStatement("INSERT INTO player(pname,pusername,pemail, ppassword, ptoken) VALUES (?,?, ?, ?, ?) returning pid")
                .use { stmt ->
                    stmt.setString(1, name)
                    stmt.setString(2, username)
                    stmt.setString(3, email)
                    stmt.setString(4, hashWithoutSalt(password))
                    stmt.setObject(5, token)
                    val rs = stmt.executeQuery()
                    if (rs.next())
                        return rs.getInt("pid")
                }
        }
        return -1
    }

    override fun login(username: String, password: String): Int {
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT * FROM player WHERE pusername = ? AND ppassword = ?").use { stmt ->
                stmt.setString(1, username)
                stmt.setString(2, password)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return rs.getInt("pid")
                }
            }
        }
        return -1
    }

    override fun getPlayerById(id: UInt): PlayerDC {
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT * FROM player WHERE pid = ?").use { stmt ->
                stmt.setInt(1, id.toInt())
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return PlayerDC(
                        rs.getInt("pid").toUInt(),
                        rs.getString("pname"),
                        rs.getString("pusername"),
                        rs.getString("pemail"),
                        rs.getString("ppassword"),
                        rs.getString("ptoken")
                    )
                }
            }
        }
        return PlayerDC(0u, "", "", "", "", "")
    }

    override fun getListOfPlayers(limit: Int, skip: Int): List<PlayerDC> {
        dataSource.connection.use { connection ->
            val query = "SELECT * FROM player LIMIT ? OFFSET ?"
            connection.prepareStatement(query).use { stmt ->
                stmt.setInt(1, limit)
                stmt.setInt(2, skip)
                val rs = stmt.executeQuery()
                val list = mutableListOf<PlayerDC>()
                return getPLayers(rs, list)
            }
        }
    }

    private fun getPLayers(
        rs: ResultSet,
        list: MutableList<PlayerDC>
    ): MutableList<PlayerDC> {
        while (rs.next()) {
            list.add(
                PlayerDC(
                    rs.getInt("pid").toUInt(),
                    rs.getString("pname"),
                    rs.getString("pusername"),
                    rs.getString("pemail"),
                    rs.getString("ppassword"),
                    rs.getString("ptoken")

                )
            )
        }
        return list
    }

    override fun selectAllGames(limit: Int, skip: Int): List<Game> {
        val list = mutableListOf<Game>()
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT * FROM game LIMIT ? OFFSET ?").use { stmt ->
                stmt.setInt(1, limit)
                stmt.setInt(2, skip)
                getPlayers(stmt, list)
            }
        }
        return list
    }

    private fun getPlayers(stmt: PreparedStatement, list: MutableList<Game>) {
        val rs = stmt.executeQuery()
        while (rs.next()) {
            list.add(
                buildGame(rs)
            )
        }
    }

    // - sessions


    override fun createSession(playerID: UInt, capacity: UInt, gid: UInt, date: LocalDateTime): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = formatter.format(date.toJavaLocalDateTime())

        dataSource.connection.use { connection ->

            connection.prepareStatement("INSERT INTO session(snofplayers, sgame_id, sessiondate, state) VALUES ( ?, ?, ?,?) returning sid")
                .use { stmt ->

                    stmt.setInt(1, capacity.toInt())
                    stmt.setInt(2, gid.toInt())
                    stmt.setObject(3, Timestamp.valueOf(formattedDateTime))

                    stmt.setString(4, Services.TYPESTATE.OPEN.toString())
                    val rs = stmt.executeQuery()

                    while (rs.next()) {
                        dataSource.connection.use { con ->
                            con.prepareStatement("INSERT INTO session_player(session_id, player_id) VALUES (?,?)")
                                .use { stmt1 ->
                                    stmt1.setInt(1, rs.getInt("sid"))
                                    stmt1.setInt(2, playerID.toInt())
                                    stmt1.executeUpdate()
                                    return rs.getInt("sid")
                                }
                        }
                    }
                }
        }
        return -1
    }

    override fun getDetailsOfSession(id: UInt): Session? {
        dataSource.connection.use { connection ->
            connection.prepareStatement(
                """
                      SELECT s.sid, s.snofplayers, s.sessiondate, s.state,
                g.gid, g.gname, g.gdeveloper, g.ggenre,
                p.pid, p.pname, p.pemail, p.ppassword, p.ptoken, p.pusername
                        FROM session s
                        JOIN game g ON s.sgame_id = g.gid
                        LEFT JOIN session_player sp ON s.sid = sp.session_id
                        LEFT JOIN player p ON sp.player_id = p.pid
                        WHERE s.sid = ?
                """


            ).use { stmt ->
                stmt.setInt(1, id.toInt())
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    val associatedPlayers = mutableListOf<PlayerOutput>()
                    val sid = rs.getInt("sid").toUInt()
                    val nofPlayers = rs.getInt("snofplayers")
                    val sessionDate = LocalDateTime.parse(rs.getObject("sessiondate").toString().replace(" ", "T"))
                    val game = buildGame(rs)
                    val state = rs.getString("state").toTypeState()!!
                    if (rs.getInt("pid") != 0) {
                        associatedPlayers.add(
                            PlayerOutput(
                                rs.getInt("pid").toUInt(),
                                rs.getString("pname"),
                                rs.getString("pusername"),
                                rs.getString("pemail"),
                                rs.getString("ptoken")
                            )
                        )
                    }
                    while (rs.next()) {
                        associatedPlayers.add(
                            PlayerOutput(
                                rs.getInt("pid").toUInt(),
                                rs.getString("pname"),
                                rs.getString("pusername"),
                                rs.getString("pemail"),
                                rs.getString("ptoken")
                            )
                        )
                    }
                    return Session(sid, nofPlayers, sessionDate, game, state, associatedPlayers)
                }

            }
        }
        return null
    }

    private fun buildGame(rs: ResultSet): Game {
        val genreArray = rs.getArray("ggenre").resultSet
        val genres = mutableSetOf<Genre>()
        while (genreArray.next()) {
            val genreIndex = genreArray.getInt(2) // get the array element
            genres.add(Genre.entries[genreIndex])
        }
        return Game(
            rs.getInt("gid").toUInt(),
            rs.getString("gname"),
            rs.getString("gdeveloper"),
            genres
        )
    }

    override fun getListSession(
        gid: UInt,
        date: LocalDateTime?,
        state: Services.TYPESTATE?,
        pid: UInt?
    ): List<Session> {
        val listSessions = mutableListOf<Session>()
        dataSource.connection.use { connection ->
            var query =
                """ SELECT s.sid, s.snofplayers, s.sessionDate, s.state,
                     g.gid , g.gname , g.gdeveloper, g.ggenre
                        FROM session s JOIN game g ON s.sgame_id = g.gid
                    WHERE s.sgame_id = ?
                    """
            date?.let {
                query += " AND s.sessionDate <= ?"
            }
            state?.let {
                query += " AND s.state = ?"
            }
            pid?.let {
                query += " AND s.sid IN (SELECT session_id FROM session_player WHERE player_id = ?)"
            }

            connection.prepareStatement(query).use { stmt ->
                var counter = 1
                stmt.setInt(counter, gid.toInt())
                counter++
                if (date != null) {
                    stmt.setObject(counter, Timestamp.valueOf(date.toJavaLocalDateTime()))
                    counter++
                }
                if (state != null) {
                    stmt.setString(counter, state.toString())
                    counter++
                }
                if (pid != null) {
                    stmt.setInt(counter, pid.toInt())
                }
                //  stmt.setString(3, state.toString())
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    val associatedPlayers = mutableListOf<PlayerOutput?>()
                    val sid = rs.getInt("sid").toUInt()
                    //     val date = rs.getTimestamp("sessionDate").toString().replace(" ", "T").toLocalDateTime()
                    val nofPlayers = rs.getInt("snofplayers")
                    val sessionDate = LocalDateTime.parse(rs.getObject("sessiondate").toString().replace(" ", "T"))
                    val game = buildGame(rs)
                    val typeState = rs.getString("state").toTypeState()!!

                    connection.prepareStatement("SELECT * FROM session_player join player p on p.pid = session_player.player_id WHERE session_id = ?")
                        .use { stmt2 ->
                            stmt2.setInt(1, sid.toInt())
                            val rs2 = stmt2.executeQuery()
                            while (rs2.next()) {

                                associatedPlayers.add(
                                    PlayerOutput(
                                        rs2.getInt("pid").toUInt(),
                                        rs2.getString("pname"),
                                        rs2.getString("pusername"),
                                        rs2.getString("pemail"),
                                        rs2.getString("ptoken")
                                    )
                                )
                            }
                        }
                    val nonNullAssociatedPlayers = associatedPlayers.filterNotNull().toMutableList()
                    listSessions.add(Session(sid, nofPlayers, sessionDate, game, typeState, nonNullAssociatedPlayers))
                }
            }
        }
        return listSessions
    }

    override fun addPLayerToSession(id: UInt, sid: UInt): Session {
        dataSource.connection.use { connection ->
            connection.prepareStatement(
                """
            SELECT
                s.sid AS session_id,
                s.snofplayers AS session_nofplayers,
                s.sessiondate AS session_date,
                g.gid ,
                g.gname ,
                g.gdeveloper,
                g.ggenre 
            FROM
                session s
            INNER JOIN
                game g ON s.sgame_id = g.gid
            WHERE
                s.state = 'OPEN'
                AND s.sid = ?
        """
            ).use { stmt ->
                stmt.setInt(1, sid.toInt())
                val rs = stmt.executeQuery()
                val idPLayers = mutableListOf<PlayerOutput>()
                connection.prepareStatement("INSERT INTO session_player(session_id, player_id) VALUES (?, ?)")
                    .use { stmt1 ->
                        stmt1.setInt(1, sid.toInt())
                        stmt1.setInt(2, id.toInt())
                        stmt1.executeUpdate()
                    }

                val session = if (rs.next()) {
                    Session(
                        rs.getInt("session_id").toUInt(),
                        rs.getInt("session_nofplayers"),
                        LocalDateTime.parse(rs.getTimestamp("session_date").toString().replace(" ", "T")),
                        game = buildGame(rs),
                        Services.TYPESTATE.OPEN,
                        idPLayers
                    )
                } else {

                    throw Exception("Session not found")
                }
                connection.use { _ ->
                    connection.prepareStatement("SELECT * FROM session_player WHERE session_id = ?").use { statement ->
                        statement.setInt(1, sid.toInt())
                        val rs2 = statement.executeQuery()
                        while (rs2.next()) {
                            val player = getPlayerById(rs2.getInt("player_id").toUInt())
                            idPLayers.add(
                                PlayerOutput(
                                    player.id,
                                    player.name,
                                    player.username,
                                    player.email,
                                    player.token
                                )
                            )
                        }
                    }

                }
                return session

            }

        }
    }


    override fun getPlayerToken(token: String): String? {
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT ptoken FROM player WHERE ptoken = ?").use { stmt ->
                // Aqui estamos convertendo a String token para UUID
                val uuidToken: UUID = UUID.fromString(token)
                stmt.setObject(1, uuidToken)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return rs.getObject("ptoken").toString()
                }
            }
        }
        return null
    }


    override fun getPlayerByEmail(email: String): PlayerDC? {
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT * FROM player WHERE pemail = ?").use { stmt ->
                stmt.setString(1, email)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return PlayerDC(
                        rs.getInt("pid").toUInt(),
                        rs.getString("pname"),
                        rs.getString("pusername"),
                        rs.getString("pemail"),
                        rs.getString("ppassword"),
                        rs.getString("ptoken")
                    )
                }
            }
        }
        return null
    }

    override fun getPlayerByUsername(username: String): PlayerDC? {
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT * FROM player WHERE pusername = ?").use { stmt ->
                stmt.setString(1, username)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return PlayerDC(
                        rs.getInt("pid").toUInt(),
                        rs.getString("pname"),
                        rs.getString("pusername"),
                        rs.getString("pemail"),
                        rs.getString("ppassword"),
                        rs.getString("ptoken")
                    )
                }
            }
        }
        return null
    }

    override fun deleteSession(sid: UInt): Boolean {
        dataSource.connection.use { connection ->
            connection.prepareStatement("DELETE FROM session_player WHERE session_id = ?").use { stmt ->
                stmt.setInt(1, sid.toInt())
                stmt.executeUpdate()
            }
            connection.prepareStatement("DELETE FROM session WHERE sid = ?").use { stmt ->
                stmt.setInt(1, sid.toInt())
                val rs = stmt.executeUpdate()
                return rs > 0
            }
        }
    }
/*
    fun updateSession1(sid: UInt, capacity: UInt?, date: LocalDateTime?, state: Services.TYPESTATE?): Session {
        dataSource.connection.use { connection ->
            if(date != null){
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formattedDateTime = formatter.format(date.toJavaLocalDateTime())
            }
            if(state != null){
                state.toString()
            }
            if(capacity != null){
                capacity.toInt()
            }

            connection.prepareStatement("UPDATE session SET snofplayers = ?, sessiondate = ?, state = ? WHERE sid = ?")
                .use { stmt ->
                    capacity?.let { stmt.setInt(1, it.toInt()) }
                    stmt.setObject(2, Timestamp.valueOf(formattedDateTime))
                    stmt.setString(3, state.toString())
                    stmt.setInt(4, sid.toInt())
                    val rs = stmt.executeUpdate()
                    if (rs > 0) {
                        return getDetailsOfSession(sid)!!
                    }
                }
        }
        throw IllegalArgumentException("Session not found")
    }



 */
override fun updateSession(sid: UInt, capacity: UInt?, date: LocalDateTime?, state: Services.TYPESTATE?): Session {
    dataSource.connection.use { connection ->
        val updateParts = mutableListOf<String>()
        val parameters = mutableListOf<Any>()

        capacity?.let {
            updateParts.add("snofplayers = ?")
            parameters.add(it.toInt())
        }

        date?.let {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = formatter.format(it.toJavaLocalDateTime())
            updateParts.add("sessiondate = ?")
            parameters.add(Timestamp.valueOf(formattedDateTime))
        }

        state?.let {
            updateParts.add("state = ?")
            parameters.add(it.toString())
        }

        if (updateParts.isNotEmpty()) {
            val updateSQL = updateParts.joinToString(", ")
            val sql = "UPDATE session SET $updateSQL WHERE sid = ?"
            connection.prepareStatement(sql).use { stmt ->
                parameters.forEachIndexed { index, parameter ->
                    stmt.setObject(index + 1, parameter)
                }
                stmt.setInt(parameters.size + 1, sid.toInt())
                val rs = stmt.executeUpdate()
                if (rs > 0) {
                    return getDetailsOfSession(sid)!!
                }
            }
        }
    }
    throw IllegalArgumentException("Session not found")
}

    override fun removePlayerFromSession(id: UInt, sID: UInt): Session {
        dataSource.connection.use { connection ->
            connection.prepareStatement("DELETE FROM session_player WHERE session_id = ? AND player_id = ?")
                .use { stmt ->
                    stmt.setInt(1, sID.toInt())
                    stmt.setInt(2, id.toInt())
                    val rs = stmt.executeUpdate()
                    if (rs > 0) {
                        return getDetailsOfSession(sID)!!
                    }
                }
        }
        throw IllegalArgumentException("Session not found")
    }

    override fun getSessionsOfPlayer(id: UInt): List<Session> {
        val list = mutableListOf<Session>()
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT session_id FROM session_player JOIN public.session s on s.sid = session_player.session_id WHERE player_id = ?")
                .use { stmt ->
                    stmt.setInt(1, id.toInt())
                    val rs = stmt.executeQuery()
                    while (rs.next()) {
                        val session = getDetailsOfSession(rs.getInt("session_id").toUInt())
                        if (session != null) {
                            list.add(session)
                        }

                    }
                }
        }
        return list
    }

    override fun getGameListByName(name: String): List<Game> {
        val games = mutableListOf<Game>()
        dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT * FROM game WHERE gname LIKE ?").use { stmt ->
                stmt.setString(1, "$name%") // The '%' is a wildcard that matches any sequence of characters
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    games.add(buildGame(rs))
                }
            }
        }
        return games
    }
}






