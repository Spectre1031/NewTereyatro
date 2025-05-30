package com.example.myapplication.data

import android.content.Context
import androidx.room.Room

object DatabaseModule {
    fun getDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "movies_database"
        ).build()
}
