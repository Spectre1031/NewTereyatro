// app/src/main/java/com/example/myapplication/data/MovieDao.kt
package com.example.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM mmff_movies")
    fun getAllMovies(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(movie: Movie)

    @Query("""
    UPDATE mmff_movies
    SET isWatchlisted = :watchlisted
    WHERE id = :id
  """)

    suspend fun updateWatchlistFlag(id: String, watchlisted: Boolean)

    @Query("SELECT * FROM mmff_movies WHERE isWatchlisted = 1")
    fun getWatchlistFlow(): Flow<List<Movie>>

    @Query("SELECT DISTINCT year FROM mmff_movies ORDER BY year")
    fun getDistinctYears(): Flow<List<Int>>

    @Query("SELECT * FROM mmff_movies WHERE id = :id")
    fun getMovieById(id: Int): Flow<Movie?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Query("DELETE FROM mmff_movies WHERE id = :id")
    suspend fun deleteById(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>): List<Long>

    @Query("UPDATE mmff_movies SET isWatchlisted = :watch WHERE id = :id")
    suspend fun setWatchlisted  (id: Int, watch: Boolean)

    @Update
    suspend fun update(movie: Movie)

    @Query("DELETE FROM mmff_movies")
    suspend fun deleteAll(): Int
}
