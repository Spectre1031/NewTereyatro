package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private var dao: MovieDao
) {

    val watchlist: Flow<List<Movie>> = dao.getWatchlistFlow()

    fun getYears(): Flow<List<Int>> = dao.getDistinctYears()

    fun getAllMovies(): Flow<List<Movie>> = dao.getAllMovies()

    fun getMovieById(id: Int): Flow<Movie?> = dao.getMovieById(id)

    suspend fun updateMovie(movie: Movie) { dao.update(movie) }

    val watchlistFlow: Flow<List<Movie>> = dao.getWatchlistFlow()

    suspend fun setWatchlistFlag(id: Int, watchlisted: Boolean) { dao.updateWatchlistFlag(id.toString(), watchlisted) }

    suspend fun update(movie: Movie) = updateMovie(movie)

    suspend fun insertAll(movies: List<Movie>) { dao.insertAll(movies) }

    suspend fun setWatchlisted(id: Int, watch: Boolean) { dao.setWatchlisted(id, watch) }

    suspend fun removeFromWatchlist(id: String) { dao.updateWatchlistFlag(id, false) }

    suspend fun addToWatchlist(movie: Movie) { dao.upsert(movie.copy(isWatchlisted = true)) }
}
