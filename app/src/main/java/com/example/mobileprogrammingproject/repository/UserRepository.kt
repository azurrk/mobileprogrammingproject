package com.example.mobileprogrammingproject.repository

import com.example.mobileprogrammingproject.model.User

interface UserRepository: BaseRepository<User> {
    suspend fun getUserById(id: Int): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun getUserByEmailAndPassword(email: String, password: String): User?;
}