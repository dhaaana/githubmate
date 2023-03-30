package com.dhana.githubmate.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val users: List<UserResponse>
)

data class UserResponse(
    val login: String,
    val id: Long,
    @SerializedName("avatar_url")
    val avatarUrl: String,
)

data class UserDetailResponse(
    val login: String,
    val id: Long,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    val name: String?,
    val company: String?,
    val location: String?,
    val bio: String?,
    val followers: Int,
    val following: Int
)
