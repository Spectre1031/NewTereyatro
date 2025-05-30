// <projectRoot>/app/src/main/java/com/example/myapplication/di/AppModule.kt
package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        AppDatabase.getInstance(ctx)

    @Provides
    fun provideMovieDao(db: AppDatabase): MovieDao =
        db.movieDao()

    @Provides @Singleton
    fun provideRepository(dao: MovieDao): MovieRepository =
        MovieRepository(dao)
}
