package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val dao: MovieDao
) {

    // your existing Flows
    val watchlist: Flow<List<Movie>>         = dao.getWatchlistFlow()
    val watchlistFlow: Flow<List<Movie>>     = dao.getWatchlistFlow()

    fun getYears(): Flow<List<Int>>          = dao.getDistinctYears()
    fun getAllMovies(): Flow<List<Movie>>    = dao.getAllMovies()
    fun getMovieById(id: Int): Flow<Movie?>  = dao.getMovieById(id)

    // ✅ new: fetch one movie, suspending until the first emission
    suspend fun getMovieOnce(id: Int): Movie =
        dao.getMovieById(id).first()!!
    // use `first() ?: throw …` if you want a custom error on missing movie

    // your existing suspend helpers
    suspend fun updateMovie(movie: Movie)            = dao.update(movie)
    suspend fun update(movie: Movie)                 = updateMovie(movie)
    suspend fun insertAll(movies: List<Movie>)       = dao.insertAll(movies)

    suspend fun setWatchlisted(id: Int, watch: Boolean) {
        dao.setWatchlisted(id, watch)
    }
    suspend fun setWatchlistFlag(id: Int, watchlisted: Boolean) {
        dao.updateWatchlistFlag(id.toString(), watchlisted)
    }

    suspend fun removeFromWatchlist(id: String)      = dao.updateWatchlistFlag(id, false)
    suspend fun addToWatchlist(movie: Movie)         = dao.upsert(movie.copy(isWatchlisted = true))
}
