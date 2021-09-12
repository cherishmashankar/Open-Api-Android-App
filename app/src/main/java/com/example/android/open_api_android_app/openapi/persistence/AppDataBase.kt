package com.example.android.open_api_android_app.openapi.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.open_api_android_app.openapi.models.AccountProperties
import com.example.android.open_api_android_app.openapi.models.AuthToken


@Database(entities = [AuthToken::class, AccountProperties::class], version = 1)
abstract class AppDataBase: RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    companion object{
        const val DATABASE_NAME = "app_db"
    }
}