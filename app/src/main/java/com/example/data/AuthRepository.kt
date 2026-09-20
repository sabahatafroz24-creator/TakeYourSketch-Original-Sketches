package com.example.data

import kotlinx.coroutines.flow.Flow
import java.util.UUID

sealed class AuthResult {
    data class Success(val user: SessionEntity) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository(private val userDao: UserDao) {
    val currentUser: Flow<SessionEntity?> = userDao.getActiveSessionFlow()

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    suspend fun signUp(
        emailRaw: String,
        passwordRaw: String,
        displayNameRaw: String
    ): AuthResult {
        val email = emailRaw.trim().lowercase()
        val displayName = displayNameRaw.trim()
        val password = passwordRaw

        if (email.isBlank() || !isValidEmail(email)) {
            return AuthResult.Error("Please enter a valid email address.")
        }
        if (displayName.isBlank()) {
            return AuthResult.Error("Please enter your name.")
        }
        if (password.length < 6) {
            return AuthResult.Error("Password must be at least 6 characters.")
        }

        val existingUser = userDao.getUserByEmail(email)
        if (existingUser != null) {
            return AuthResult.Error("An account with this email already exists.")
        }

        val salt = SecurityUtil.generateSalt()
        val passwordHash = SecurityUtil.hashPassword(password, salt)
        val userId = "usr_" + UUID.randomUUID().toString().replace("-", "").take(12)

        val newUser = UserEntity(
            id = userId,
            email = email,
            displayName = displayName,
            passwordHash = passwordHash,
            passwordSalt = salt
        )

        userDao.insertUser(newUser)

        val session = SessionEntity(
            userId = newUser.id,
            email = newUser.email,
            displayName = newUser.displayName
        )
        userDao.setActiveSession(session)
        return AuthResult.Success(session)
    }

    suspend fun logIn(emailRaw: String, passwordRaw: String): AuthResult {
        val email = emailRaw.trim().lowercase()
        val password = passwordRaw

        if (email.isBlank() || !isValidEmail(email)) {
            return AuthResult.Error("Please enter a valid email address.")
        }
        if (password.isBlank()) {
            return AuthResult.Error("Please enter your password.")
        }

        val user = userDao.getUserByEmail(email)
            ?: return AuthResult.Error("Invalid email or password.")

        val isPasswordValid = SecurityUtil.verifyPassword(password, user.passwordSalt, user.passwordHash)
        if (!isPasswordValid) {
            return AuthResult.Error("Invalid email or password.")
        }

        val session = SessionEntity(
            userId = user.id,
            email = user.email,
            displayName = user.displayName
        )
        userDao.setActiveSession(session)
        return AuthResult.Success(session)
    }

    suspend fun logOut() {
        userDao.clearActiveSession()
    }

    private fun isValidEmail(email: String): Boolean {
        return emailRegex.matches(email)
    }
}
