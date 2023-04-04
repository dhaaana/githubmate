package com.dhana.githubmate.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dhana.githubmate.databinding.ActivitySettingBinding
import com.dhana.githubmate.ui.viewmodel.SettingViewModel
import com.dhana.githubmate.ui.viewmodel.ViewModelFactory


class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val settingViewModel: SettingViewModel by viewModels() {
        ViewModelFactory.getInstance(application, dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val backButton = binding.btnBack
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val switchTheme = binding.switchTheme

        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }
}