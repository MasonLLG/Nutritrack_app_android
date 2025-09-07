package com.fit2081assignment3.nutritrack.data.repository

import com.fit2081assignment3.nutritrack.data.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun createUser(user: User): Long = userDao.insertUser(user)
    suspend fun getUser(userId: String): User? = userDao.getUserByUserId(userId)
    suspend fun updateUser(user: User): Int = userDao.updateUser(user)
}
