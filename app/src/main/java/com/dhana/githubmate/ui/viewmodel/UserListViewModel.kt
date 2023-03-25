package com.dhana.githubmate.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhana.githubmate.api.ApiConfig
import com.dhana.githubmate.api.ApiService
import com.dhana.githubmate.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListViewModel() : ViewModel() {

    private val _userList = MutableLiveData<List<UserResponse>>()
    val userList: LiveData<List<UserResponse>> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "UserListViewModel"
    }

    init {
        getUsers()
    }

    private fun getUsers(query: String? = null) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers()

        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                if (response.isSuccessful) {
                    _userList.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}

