package com.dhana.githubmate.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhana.githubmate.data.remote.response.UserResponse
import com.dhana.githubmate.databinding.ActivityFavoriteBinding
import com.dhana.githubmate.ui.adapter.UserListAdapter
import com.dhana.githubmate.ui.viewmodel.FavoriteUserViewModel
import com.dhana.githubmate.ui.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val favoriteUserViewModel: FavoriteUserViewModel by viewModels() {
        ViewModelFactory.getInstance(application, dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        favoriteUserViewModel.getFavoriteUsers().observe(this) {
            setUserListData(it)
        }
    }

    private fun setUserListData(userList: List<UserResponse>) {
        val adapter = UserListAdapter(userList)
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponse) {
                val detailIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                startActivity(detailIntent)
            }
        })
    }
}