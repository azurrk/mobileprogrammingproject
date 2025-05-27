package com.example.mobileprogrammingproject.repository


import com.example.mobileprogrammingproject.dao.UserDao;
import com.example.mobileprogrammingproject.model.User;

import javax.inject.Inject;

class UserRepositoryImpl @Inject
constructor(private val userDao:UserDao): UserRepository {
    override suspend fun insert(entity:User) {
        return userDao.insert(entity)
    }

    override suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }


    override suspend fun getUserByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmailAndPassword(email: String, password: String): User? {
        return userDao.getUserByEmailAndPassword(email, password)
    }

    override suspend fun delete(entity: User) {
        return userDao.delete(entity)
    }

    override suspend fun update(entity: User) {
        return userDao.update(entity)
    }
}
