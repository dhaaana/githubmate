package com.dhana.githubmate.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.dhana.githubmate.R
import com.dhana.githubmate.databinding.ActivityDetailBinding
import com.dhana.githubmate.model.UserResponse
import com.dhana.githubmate.ui.adapter.SectionsPagerAdapter
import com.dhana.githubmate.ui.viewmodel.UserDetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

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

        val detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserDetailViewModel::class.java]

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            detailViewModel.getUserDetail(username)
            initializeFollowTabs(username)
        }

        detailViewModel.userDetail.observe(this) { userDetail ->
            setUserListData(userDetail)
        }
    }

    private fun setUserListData(userDetail: UserResponse) {
        binding.tvUsername.text = userDetail.login
    }

    private fun initializeFollowTabs(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }
}