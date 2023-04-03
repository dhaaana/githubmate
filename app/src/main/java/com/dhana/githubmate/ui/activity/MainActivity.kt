package com.dhana.githubmate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhana.githubmate.R
import com.dhana.githubmate.databinding.ActivityMainBinding
import com.dhana.githubmate.data.remote.response.UserResponse
import com.dhana.githubmate.ui.adapter.UserListAdapter
import com.dhana.githubmate.ui.viewmodel.SettingViewModel
import com.dhana.githubmate.ui.viewmodel.UserListViewModel
import com.dhana.githubmate.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val settingViewModel: SettingViewModel by viewModels() {
        ViewModelFactory.getInstance(application, dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserListViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        mainViewModel.userList.observe(this) { userList ->
            setUserListData(userList)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isSearching.observe(this) {
            showHomeButton(it)
        }

        mainViewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUsers(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        val homeButton = binding.home
        homeButton.setOnClickListener {
            mainViewModel.getUsers()
        }

        val optionsMenuButton = binding.options
        optionsMenuButton.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.favoriteActivity -> {
                        val favoriteIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(favoriteIntent)
                        true
                    }
                    R.id.settingActivity -> {
                        val settingIntent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(settingIntent)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    private fun setUserListData(userList: List<UserResponse>) {
        val adapter = UserListAdapter(userList)
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponse) {
                val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                startActivity(detailIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUser.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showHomeButton(isSearching: Boolean) {
        binding.home.visibility = if (isSearching) View.VISIBLE else View.GONE
    }
}
