package com.dhana.githubmate.data.remote.api

import com.dhana.githubmate.data.remote.response.SearchResponse
import com.dhana.githubmate.data.remote.response.UserDetailResponse
import com.dhana.githubmate.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Endpoint to get all users
    @GET("users")
    fun getUsers(): Call<List<UserResponse>>

    // Endpoint to search for users based on username
    @GET("search/users")
    fun searchUsers(
        @Query("q") username: String
    ): Call<SearchResponse>

    // Endpoint to get detailed user information based on username
    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    // Endpoint to get the list of followers of a user based on username
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserResponse>>

    // Endpoint to get the list of users that a user is following based on username
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserResponse>>
}
