package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieViewModel : ViewModel() {

    private val _searchResults = MutableStateFlow(MovieRepository.movies)
    val searchResults: StateFlow<List<Movie>> = _searchResults

    fun performSearch(query: String) {
        val terms = query
            .trim()
            .split(Regex("\\s+"))
            .filter { it.isNotEmpty() }
        _searchResults.value =
            if (terms.isEmpty()) {
                MovieRepository.movies
            } else {
                MovieRepository.movies.filter { movie ->
                    terms.all { term ->
                        movie.title.contains(term, ignoreCase = true)
                    }
                }
            }
    }
}