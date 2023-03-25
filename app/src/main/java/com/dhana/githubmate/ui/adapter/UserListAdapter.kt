package com.dhana.githubmate.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhana.githubmate.databinding.ItemUserBinding
import com.dhana.githubmate.model.UserResponse

class UserListAdapter(private val userList: List<UserResponse>) :
    RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.binding.tvItem.text = user.login
    }

    override fun getItemCount(): Int = userList.size

    class ViewHolder(var binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)
}