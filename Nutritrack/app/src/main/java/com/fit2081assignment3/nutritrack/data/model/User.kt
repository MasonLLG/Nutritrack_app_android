package com.fit2081assignment3.nutritrack.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "uid") val userId: String,
    val username: String,
    val gender: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    val password: String,

    val persona: String? = null,
    val mealTime: String? = null,
    val sleepTime: String? = null,
    val wakeTime: String? = null,
    val foodPreferences: String? = null
)