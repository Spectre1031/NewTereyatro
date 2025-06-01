package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Movie
import com.example.myapplication.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repo: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // pull movieId exactly once
    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    // simple flow of just the Movie row
    val movieFlow: StateFlow<Movie?> =
        repo.getMovieById(movieId)
            .stateIn(viewModelScope, SharingStarted.Lazily, null)


    fun addToWatchlist() {
        viewModelScope.launch {
            movieFlow.value?.let { repo.addToWatchlist(it) }
        }
    }
}
