package com.dhana.githubmate.data

import androidx.lifecycle.LiveData
import com.dhana.githubmate.data.local.entity.FavoriteUserEntity
import com.dhana.githubmate.data.local.room.FavoriteUserDao

class UserRepository private constructor(
    private val favoriteUserDao: FavoriteUserDao,
) {

    fun getFavoriteUsers(): LiveData<List<FavoriteUserEntity>> {
        return favoriteUserDao.getFavoriteUsers()
    }

    suspend fun insertFavoriteUser(user: FavoriteUserEntity) {
        return favoriteUserDao.insertFavoriteUser(user)
    }

    suspend fun deleteFavoriteUser(login: String) {
        return favoriteUserDao.deleteFavoriteUser(login)
    }

    fun isUserFavorite(login: String): LiveData<Boolean> {
        return favoriteUserDao.isUserFavorite(login)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            favoriteUserDao: FavoriteUserDao,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(favoriteUserDao)
            }.also { instance = it }
    }
}