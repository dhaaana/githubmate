package com.dhana.githubmate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.dhana.githubmate.data.UserRepository
import com.dhana.githubmate.data.local.entity.FavoriteUserEntity
import com.dhana.githubmate.data.remote.response.UserResponse

class FavoriteUserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getFavoriteUsers(): LiveData<List<UserResponse>> {
        return userRepository.getFavoriteUsers().map { favoriteUsers ->
            favoriteUsers.map { it.toUserResponse() }
        }
    }

    private fun FavoriteUserEntity.toUserResponse(): UserResponse {
        return UserResponse(
            login = login,
            avatarUrl = avatarUrl,
        )
    }
}


