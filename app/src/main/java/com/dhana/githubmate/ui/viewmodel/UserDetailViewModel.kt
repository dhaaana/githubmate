package com.dhana.githubmate.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhana.githubmate.data.UserRepository
import com.dhana.githubmate.data.local.entity.FavoriteUserEntity
import com.dhana.githubmate.data.remote.api.ApiConfig
import com.dhana.githubmate.data.remote.response.UserDetailResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object {
        private const val TAG = "UserDetailViewModel"
    }

    fun getUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(username)

        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message.toString()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun insertFavoriteUser(user: FavoriteUserEntity) {
        viewModelScope.launch {
            userRepository.insertFavoriteUser(user)
        }
    }

    fun deleteFavoriteUser(login: String) {
        viewModelScope.launch {
            userRepository.deleteFavoriteUser(login)
        }
    }

    fun isUserFavorite(login: String): LiveData<Boolean> {
       return userRepository.isUserFavorite(login)
    }
}


