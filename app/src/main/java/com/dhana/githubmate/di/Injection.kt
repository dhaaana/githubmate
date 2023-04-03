package com.dhana.githubmate.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dhana.githubmate.data.SettingRepository
import com.dhana.githubmate.data.UserRepository
import com.dhana.githubmate.data.local.datastore.SettingPreferences
import com.dhana.githubmate.data.local.room.FavoriteUserDatabase
import com.dhana.githubmate.data.remote.api.ApiConfig
import com.dhana.githubmate.utils.AppExecutors

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = FavoriteUserDatabase.getInstance(context)
        val dao = database.favoriteUserDao()
        return UserRepository.getInstance(dao)
    }

    fun provideSettingRepository(dataStore: DataStore<Preferences>): SettingRepository {
        val pref = SettingPreferences.getInstance(dataStore)
        return SettingRepository.getInstance(pref)
    }
}