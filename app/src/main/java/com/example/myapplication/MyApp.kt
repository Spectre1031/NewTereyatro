package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.example.myapplication.data.MovieRepository

@HiltAndroidApp
class MyApp : Application() {
    companion object {
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
