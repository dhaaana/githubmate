package com.dhana.githubmate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dhana.githubmate.data.SettingRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val settingRepository: SettingRepository) : ViewModel() {
    fun getThemeSetting(): LiveData<Boolean> {
        return settingRepository.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            settingRepository.saveThemeSetting(isDarkModeActive)
        }
    }
}