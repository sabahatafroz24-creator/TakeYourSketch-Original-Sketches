package com.example.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val displayName: String,
    val passwordHash: String,
    val passwordSalt: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "active_session")
data class SessionEntity(
    @PrimaryKey
    val id: Int = 1,
    val userId: String,
    val email: String,
    val displayName: String,
    val loggedInAt: Long = System.currentTimeMillis()
)
