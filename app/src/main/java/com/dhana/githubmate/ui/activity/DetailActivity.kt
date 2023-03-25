package com.dhana.githubmate.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dhana.githubmate.databinding.ActivityDetailBinding
import com.dhana.githubmate.databinding.ActivityMainBinding
import com.dhana.githubmate.model.UserResponse
import com.dhana.githubmate.ui.adapter.UserListAdapter
import com.dhana.githubmate.ui.viewmodel.UserDetailViewModel
import com.dhana.githubmate.ui.viewmodel.UserListViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserDetailViewModel::class.java]

        val username = intent.getStringExtra("username")
        if (username != null) {
            detailViewModel.getUserDetail(username)
        }

        detailViewModel.userDetail.observe(this) { userDetail ->
            setUserListData(userDetail)
        }
    }

    private fun setUserListData(userDetail: UserResponse) {
        binding.tvUsername.text = userDetail.login
    }
}