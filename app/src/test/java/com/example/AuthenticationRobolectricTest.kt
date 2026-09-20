package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.AuthRepository
import com.example.data.AuthResult
import com.example.data.SecurityUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AuthenticationRobolectricTest {

    private lateinit var database: AppDatabase
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        authRepository = AuthRepository(database.userDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `security util generates distinct salts and validates password hashing`() {
        val salt1 = SecurityUtil.generateSalt()
        val salt2 = SecurityUtil.generateSalt()
        assertNotEquals(salt1, salt2)

        val pass = "SecretP@ssw0rd!"
        val hash = SecurityUtil.hashPassword(pass, salt1)

        // Hashed password must NOT be plain text
        assertNotEquals(pass, hash)

        // Validating password
        assertTrue(SecurityUtil.verifyPassword(pass, salt1, hash))
        assertFalse(SecurityUtil.verifyPassword("WrongPassword", salt1, hash))
    }

    @Test
    fun `sign up creates user with securely hashed credentials and establishes session`() = runBlocking {
        val result = authRepository.signUp(
            emailRaw = "Sabahatafroz24@gmail.com",
            passwordRaw = "studio2026",
            displayNameRaw = "Sabahat Afroz"
        )

        assertTrue("Sign up should succeed", result is AuthResult.Success)
        val userSession = (result as AuthResult.Success).user
        assertEquals("sabahatafroz24@gmail.com", userSession.email)
        assertEquals("Sabahat Afroz", userSession.displayName)

        // Verify stored user in database
        val storedUser = database.userDao().getUserByEmail("sabahatafroz24@gmail.com")
        assertNotNull(storedUser)
        // Ensure plain text password is NEVER stored in database
        assertNotEquals("studio2026", storedUser!!.passwordHash)
        assertTrue(SecurityUtil.verifyPassword("studio2026", storedUser.passwordSalt, storedUser.passwordHash))

        // Verify active session flow
        val current = authRepository.currentUser.first()
        assertNotNull(current)
        assertEquals("sabahatafroz24@gmail.com", current!!.email)
    }

    @Test
    fun `sign up rejects invalid email or short password or duplicate email`() = runBlocking {
        // Invalid email
        val invalidEmailResult = authRepository.signUp("invalid-email", "pass123", "Test User")
        assertTrue(invalidEmailResult is AuthResult.Error)

        // Password too short (< 6 chars)
        val shortPassResult = authRepository.signUp("valid@test.com", "123", "Test User")
        assertTrue(shortPassResult is AuthResult.Error)

        // Blank name
        val blankNameResult = authRepository.signUp("valid@test.com", "123456", "   ")
        assertTrue(blankNameResult is AuthResult.Error)

        // Duplicate email
        val firstResult = authRepository.signUp("artist@tys.com", "secure123", "Artist")
        assertTrue(firstResult is AuthResult.Success)

        val dupResult = authRepository.signUp("artist@tys.com", "anotherpass", "Duplicate")
        assertTrue(dupResult is AuthResult.Error)
        assertEquals("An account with this email already exists.", (dupResult as AuthResult.Error).message)
    }

    @Test
    fun `login with correct credentials succeeds and invalid credentials fails`() = runBlocking {
        authRepository.signUp("collector@tys.com", "galleryPage1", "Collector")
        authRepository.logOut()

        // Verify logged out
        assertNull(authRepository.currentUser.first())

        // Wrong password
        val wrongPassResult = authRepository.logIn("collector@tys.com", "wrongPass")
        assertTrue(wrongPassResult is AuthResult.Error)

        // Non-existent email
        val wrongEmailResult = authRepository.logIn("unknown@tys.com", "galleryPage1")
        assertTrue(wrongEmailResult is AuthResult.Error)

        // Correct credentials
        val correctResult = authRepository.logIn("collector@tys.com", "galleryPage1")
        assertTrue(correctResult is AuthResult.Success)
        val session = (correctResult as AuthResult.Success).user
        assertEquals("collector@tys.com", session.email)

        // Active session is updated
        assertEquals("collector@tys.com", authRepository.currentUser.first()?.email)
    }

    @Test
    fun `logout clears active session`() = runBlocking {
        authRepository.signUp("member@tys.com", "pass12345", "Member")
        assertNotNull(authRepository.currentUser.first())

        authRepository.logOut()
        assertNull(authRepository.currentUser.first())
    }
}
