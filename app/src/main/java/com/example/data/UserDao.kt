package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity)

    // Active session handling
    @Query("SELECT * FROM active_session WHERE id = 1 LIMIT 1")
    fun getActiveSessionFlow(): Flow<SessionEntity?>

    @Query("SELECT * FROM active_session WHERE id = 1 LIMIT 1")
    suspend fun getActiveSession(): SessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setActiveSession(session: SessionEntity)

    @Query("DELETE FROM active_session")
    suspend fun clearActiveSession()
}
