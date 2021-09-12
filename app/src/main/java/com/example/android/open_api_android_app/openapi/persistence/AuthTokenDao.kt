package com.example.android.open_api_android_app.openapi.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.open_api_android_app.openapi.models.AuthToken

@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(authToken: AuthToken): Long

    @Query("UPDATE auth_token SET token = null WHERE account_pk = :pk")
    fun nullifyToken(pk: Int): Int


}