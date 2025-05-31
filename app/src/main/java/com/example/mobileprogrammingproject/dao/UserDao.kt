package com.example.mobileprogrammingproject.dao

import androidx.room.Query
import com.example.mobileprogrammingproject.model.User

interface UserDao: BaseDao<User> {
    @Query("SELECT * FROM users WHERE users.email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend  fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?
}