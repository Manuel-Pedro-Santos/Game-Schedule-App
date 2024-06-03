package pt.isel.ls.utils

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * This function generates a SecretKeySpec object that can be used as an AES key.
 * The key is hardcoded to "Grupo5LSGrupo5LS", which is a 16-character string suitable for a 128-bit AES key.
 *
 * @return A SecretKeySpec object representing the AES key.
 */
fun generateKey(): SecretKeySpec {
    val key = "Grupo5LSGrupo5LS" // 16 characters for 128-bit key
    return SecretKeySpec(key.toByteArray(), "AES")
}

/**
 * This function encrypts a given input string using AES encryption.
 * The encryption key is obtained by calling the generateKey function.
 * The encrypted string is then encoded using Base64 and returned.
 *
 * @param input The string to be encrypted.
 * @return The encrypted string, encoded in Base64.
 */
fun hashWithoutSalt(input: String): String {
    val key = generateKey()
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    val encryptedBytes = cipher.doFinal(input.toByteArray())
    val pass = Base64.getEncoder().encodeToString(encryptedBytes)
    return pass
}