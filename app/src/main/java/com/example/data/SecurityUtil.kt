package com.example.data

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Utility for cryptographically secure password hashing and verification using PBKDF2 with salt.
 */
object SecurityUtil {
    private const val ITERATIONS = 10000
    private const val KEY_LENGTH = 256
    private const val SALT_LENGTH = 16

    private val secretKeyFactory: SecretKeyFactory by lazy {
        try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        } catch (e: Exception) {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        }
    }

    fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        random.nextBytes(salt)
        return salt.toHexString()
    }

    fun hashPassword(password: String, saltHex: String): String {
        val salt = saltHex.decodeHex()
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        val hash = secretKeyFactory.generateSecret(spec).encoded
        return hash.toHexString()
    }

    fun verifyPassword(password: String, saltHex: String, expectedHashHex: String): Boolean {
        val calculatedHash = hashPassword(password, saltHex)
        return MessageDigest.isEqual(calculatedHash.toByteArray(), expectedHashHex.toByteArray())
    }

    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    private fun String.decodeHex(): ByteArray {
        check(length % 2 == 0) { "Hex string must have an even length" }
        return chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }
}
