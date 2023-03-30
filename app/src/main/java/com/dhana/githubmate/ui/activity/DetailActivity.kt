package com.dhana.githubmate.ui.activity

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dhana.githubmate.R
import com.dhana.githubmate.databinding.ActivityDetailBinding
import com.dhana.githubmate.model.UserDetailResponse
import com.dhana.githubmate.ui.adapter.SectionsPagerAdapter
import com.dhana.githubmate.ui.viewmodel.UserDetailViewModel
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

        val backButton = binding.btnBack
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserDetailViewModel::class.java]

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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            setContentView(binding.root)
//        } else {
//            setContentView(binding.root)
//        }
//    }
}