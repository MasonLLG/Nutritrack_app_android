package com.fit2081assignment3.nutritrack.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fit2081assignment3.nutritrack.data.model.Nutrition
import com.fit2081assignment3.nutritrack.data.model.User


@Database(
    entities = [User::class, Nutrition::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun nutritionDao(): NutritionDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}
