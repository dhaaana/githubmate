package com.dhana.githubmate.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dhana.githubmate.R
import com.dhana.githubmate.data.local.entity.FavoriteUserEntity
import com.dhana.githubmate.databinding.ActivityDetailBinding
import com.dhana.githubmate.data.remote.response.UserDetailResponse
import com.dhana.githubmate.ui.adapter.SectionsPagerAdapter
import com.dhana.githubmate.ui.viewmodel.UserDetailViewModel
import com.dhana.githubmate.ui.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Date

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val detailViewModel: UserDetailViewModel by viewModels() {
        ViewModelFactory.getInstance(application, dataStore)
    }

    companion object {
        const val EXTRA_USERNAME = "username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val backButton = binding.btnBack
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            if (detailViewModel.userDetail.value == null) {
                detailViewModel.getUserDetail(username)
            }
            initializeFollowTabs(username)
        }

        detailViewModel.userDetail.observe(this) { userDetail ->
            setupFollowTabs(listOf(userDetail.followers, userDetail.following))
            setUserDetailData(userDetail)
            initializeFavoriteButton(userDetail)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUserDetailData(userDetail: UserDetailResponse) {
        binding.tvUsername.text = userDetail.login
        binding.tvName.text = userDetail.name
        Glide.with(this).load(userDetail.avatarUrl).into(binding.profilePicture)
    }

    private fun initializeFollowTabs(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
    }

    private fun setupFollowTabs(followCount: List<Int>) {
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.setCustomView(R.layout.tab)
            val tabTitle = tab.customView?.findViewById<TextView>(R.id.tabText)
            val tabFollowCount = tab.customView?.findViewById<TextView>(R.id.followCount)
            tabTitle?.text = resources.getString(TAB_TITLES[position])
            tabFollowCount?.text = followCount[position].toString()
        }.attach()
    }

    private fun initializeFavoriteButton(userDetail: UserDetailResponse) {
        val favoriteBtn = binding.btnFav

        detailViewModel.isUserFavorite(userDetail.login).observe(this) {d ->
            binding.buttonProgressBar.visibility = View.GONE
            if (d) {
                favoriteBtn.setImageResource(R.drawable.ic_favorite)
            } else {
                favoriteBtn.setImageResource(R.drawable.ic_favorite_border)
            }

            favoriteBtn.setOnClickListener {
                if (d) {
                    detailViewModel.deleteFavoriteUser(userDetail.login)
                    Toast.makeText(this, "${userDetail.login} removed from favorite users", Toast.LENGTH_SHORT).show()
                } else {
                    val user = FavoriteUserEntity(userDetail.login, userDetail.avatarUrl)
                    detailViewModel.insertFavoriteUser(user)
                    Toast.makeText(this, "${userDetail.login} added to favorite users", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}