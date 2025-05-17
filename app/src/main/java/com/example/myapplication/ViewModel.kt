package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieViewModel : ViewModel() {
    private val _movies = MutableStateFlow(MovieRepository.movies)
    val movies: StateFlow<List<Movie>> = _movies
}