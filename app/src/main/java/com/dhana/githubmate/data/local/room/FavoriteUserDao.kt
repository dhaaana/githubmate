package com.dhana.githubmate.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dhana.githubmate.data.local.entity.FavoriteUserEntity

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM favorite")
    fun getFavoriteUsers(): LiveData<List<FavoriteUserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteUser(user: FavoriteUserEntity)

    @Query("DELETE FROM favorite WHERE login = :login")
    suspend fun deleteFavoriteUser(login: String)

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE login = :login)")
    fun isUserFavorite(login: String): LiveData<Boolean>
}