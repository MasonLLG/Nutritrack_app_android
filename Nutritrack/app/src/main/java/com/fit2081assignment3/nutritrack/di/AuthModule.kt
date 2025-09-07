package com.fit2081assignment3.nutritrack.di

import android.content.Context
import androidx.datastore.preferences.createDataStore
import com.fit2081assignment3.nutritrack.data.repository.AuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager {
        val dataStore = context.createDataStore(name = "auth_prefs")
        return AuthManager(dataStore)
    }
}