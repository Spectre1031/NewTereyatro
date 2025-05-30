// app/src/main/java/com/example/myapplication/data/MovieDao.kt
package com.example.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM mmff_movies ORDER BY title ASC")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM mmff_movies WHERE movie_id = :id")
    fun getMovieById(id: Int): Flow<Movie?>

    @Query("SELECT * FROM mmff_movies WHERE isWatchlisted = 1 ORDER BY title ASC")
    fun getWatchlistedMovies(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>): List<Long>

    @Query("UPDATE mmff_movies SET isWatchlisted = :watch WHERE movie_id = :id")
    suspend fun setWatchlisted(id: Int, watch: Boolean)

    @Update
    suspend fun update(movie: Movie)            // ← NEW!

    @Query("DELETE FROM mmff_movies")
    suspend fun deleteAll(): Int
}
