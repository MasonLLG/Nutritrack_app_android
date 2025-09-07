package com.fit2081assignment3.nutritrack.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.fit2081assignment3.nutritrack.data.repository.AppDatabase
import com.fit2081assignment3.nutritrack.data.repository.NutritionDao
import com.fit2081assignment3.nutritrack.data.repository.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideNutritionDao(db: AppDatabase): NutritionDao = db.nutritionDao()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
}