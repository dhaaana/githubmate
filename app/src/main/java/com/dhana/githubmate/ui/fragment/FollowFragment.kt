package com.dhana.githubmate.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhana.githubmate.databinding.FragmentFollowBinding
import com.dhana.githubmate.model.UserResponse
import com.dhana.githubmate.ui.activity.DetailActivity
import com.dhana.githubmate.ui.adapter.UserListAdapter
import com.dhana.githubmate.ui.viewmodel.UserFollowViewModel

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var followViewModel: UserFollowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        followViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserFollowViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME, "")

        if (username != null && index != null) {
            when (index) {
                1 -> followViewModel.getFollowers(username)
                2 -> followViewModel.getFollowing(username)
            }
        }

        followViewModel.followerList.observe(viewLifecycleOwner) {
            setUserFollowData(it)
        }

        followViewModel.followingList.observe(viewLifecycleOwner) {
            setUserFollowData(it)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setUserFollowData(userList: List<UserResponse>) {
        val adapter = UserListAdapter(userList)
        binding.rvReview.adapter = adapter
        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponse) {
                val detailIntent = Intent(activity, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                startActivity(detailIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }
}