package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Movie
import com.example.myapplication.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val allMovies: LiveData<List<Movie>> =
        repo.getAllMovies().asLiveData()

    val watchlist: LiveData<List<Movie>> =
        repo.watchlist.asLiveData()

    val yearsFlow: StateFlow<List<Int>> = repo
        .getYears()
        .map { list -> list.sortedDescending() }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addToWatchlist(movie: Movie) = viewModelScope.launch {
        repo.updateMovie(movie.copy(isWatchlisted = true))
    }

    fun removeFromWatchlist(movie: Movie) = viewModelScope.launch {
        repo.updateMovie(movie.copy(isWatchlisted = false))
    }

}
