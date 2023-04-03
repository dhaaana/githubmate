package com.dhana.githubmate.data

import com.dhana.githubmate.data.local.datastore.SettingPreferences

class SettingRepository private constructor(private val pref: SettingPreferences) {

    fun getThemeSetting() = pref.getThemeSetting()

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = pref.saveThemeSetting(isDarkModeActive)

    companion object {
        @Volatile
        private var instance: SettingRepository? = null
        fun getInstance(pref: SettingPreferences) =
            instance ?: synchronized(this) {
                instance ?: SettingRepository(pref).also { instance = it }
            }
    }
}
