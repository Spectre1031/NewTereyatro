package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val dao: MovieDao
) {
    /** Stream of all movies in alphabetical order */
    val allMovies: Flow<List<Movie>> = dao.getAllMovies()

    /** Stream of only those flagged watchlisted */
    val watchlist: Flow<List<Movie>> = dao.getWatchlistedMovies()

    /** Update a single movie row (e.g. toggle watchlist flag) */
    suspend fun updateMovie(movie: Movie) {
        dao.update(movie)
    }

    /** Alias so UI can call `repo.update(...)` directly */
    suspend fun update(movie: Movie) = updateMovie(movie)

    /** Bulk-insert seed data */
    suspend fun insertAll(movies: List<Movie>) {
        dao.insertAll(movies)
    }

    /** Toggle the watchlist flag on a movie row */
    suspend fun setWatchlisted(id: Int, watch: Boolean) {
        dao.setWatchlisted(id, watch)
    }
}
