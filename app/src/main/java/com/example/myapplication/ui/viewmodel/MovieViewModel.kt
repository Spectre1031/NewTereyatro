package com.example.myapplication.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.data.Movie
import com.example.myapplication.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.myapplication.data.MovieDao

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    // LiveData wrapping the Flow of all movies
    val allMovies: LiveData<List<Movie>> =
        repo.allMovies.asLiveData()

    // LiveData wrapping the Flow of watchlist
    val watchlist: LiveData<List<Movie>> =
        repo.watchlist.asLiveData()

    

    // toggle watchlist flag
    fun addToWatchlist(movie: Movie) = viewModelScope.launch {
        repo.updateMovie(movie.copy(isWatchlisted = true))
    }

    fun removeFromWatchlist(movie: Movie) = viewModelScope.launch {
        repo.updateMovie(movie.copy(isWatchlisted = false))
    }

    // for Movie Details screen
    fun movieById(id: Int): LiveData<Movie?> =
        repo.allMovies
            .map { list -> list.find { it.movie_id == id } }
            .asLiveData()

    fun toggleWatchlist(id: Int, watch: Boolean) {
        viewModelScope.launch {
            repo.setWatchlisted(id, watch)
        }
    }
}
