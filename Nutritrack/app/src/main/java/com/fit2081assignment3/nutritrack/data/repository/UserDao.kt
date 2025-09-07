package com.fit2081assignment3.nutritrack.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fit2081assignment3.nutritrack.data.model.User

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE uid = :userId LIMIT 1")
    fun getUserByUserId(userId: String): User?

    @Update
    fun updateUser(user: User): Int
}