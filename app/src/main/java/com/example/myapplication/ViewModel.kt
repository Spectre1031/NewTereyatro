package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieViewModel : ViewModel() {

    private val _searchResults = MutableStateFlow(MovieRepository.movies)
    val searchResults: StateFlow<List<Movie>> = _searchResults

    fun performSearch(query: String) {
        val t = query.trim()
        _searchResults.value =
            if (t.isBlank()) MovieRepository.movies
            else MovieRepository.movies.filter {
                it.title.startsWith(t, ignoreCase = true)
            }
    }
}
