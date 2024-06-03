package pt.isel.ls.domain


enum class Genre(val identifier: String) {
    ACTION("Action"), ADVENTURE("Adventure"),
    ARCADE("Arcade"), FIGHTING("Fighting"),
    RACING("Racing"), RPG("Rpg"),
    SHOOTER("Shooter"), SIMULATION("Simulation"),
    SPORTS("Sports"), STRATEGY("Strategy"),
    TURN_BASED("Turn-Based"),
}


fun classifyGenre(genreString: String): Genre {
    val lowercaseString = genreString.lowercase()
    return Genre.entries.firstOrNull { it.identifier.lowercase() == lowercaseString } ?: throw IllegalArgumentException(
        "Invalid genre"
    )
}


data class Game(
    val gameId: UInt,
    val gameName: String,
    val gameDev: String,
    val gameGenre: Set<Genre>
) {
    init {
        require(gameName.isNotBlank()) { "Game name can't be blank" }
        require(gameDev.isNotBlank()) { "Game developer can't be blank" }
        require(gameGenre.isNotEmpty()) { "Game genre can't be empty" }
    }
}








