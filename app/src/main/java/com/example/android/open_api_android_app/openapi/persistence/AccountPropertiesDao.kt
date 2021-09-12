package com.example.android.open_api_android_app.openapi.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.open_api_android_app.openapi.models.AccountProperties


@Dao
interface AccountPropertiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(accountPropertie: AccountProperties): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(accountProperties: AccountProperties): Long

    @Query("SELECT * FROM account_properties WHERE pk = :pk")
    fun searchByPK(pk: Int): AccountProperties?

    @Query("SELECT * FROM account_properties WHERE email = :email")
    fun searchByEmail(email: String): AccountProperties?


}