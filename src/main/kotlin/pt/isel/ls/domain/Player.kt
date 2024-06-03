package pt.isel.ls.domain

/**
 * @param id the id of the player
 * @param name the name of the player
 * @param email the email of the player (unique)
 * @param password the password of the player
 * @param token the token of the player
 */

data class PlayerDC(
    val id: UInt,
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val token: String

) {
    init {
        require(name.isNotEmpty()) { "Name cannot be empty" }
        require(username.isNotEmpty()) { "Username cannot be empty" }
        require(password.isNotEmpty()) { "Password cannot be empty" }
    }
}



