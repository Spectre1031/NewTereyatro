package com.example.myapplication.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Movie::class],
    version  = 2,                // <-- bumped
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(ctx: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    ctx.applicationContext,
                    AppDatabase::class.java,
                    "movies.db"
                )
                    // if you change version, wipe old data so onCreate() fires again:
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // when the empty DB is first created, insert all sampleMovies:
                            CoroutineScope(Dispatchers.IO).launch {
                                getInstance(ctx)
                                    .movieDao()
                                    .insertAll(sampleMovies)
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
