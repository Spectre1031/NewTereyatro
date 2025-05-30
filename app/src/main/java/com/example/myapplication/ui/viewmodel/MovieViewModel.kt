package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.data.Movie
import com.example.myapplication.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repo: MovieRepository,
    savedState: SavedStateHandle
    ) : ViewModel() {

    val allMovies: LiveData<List<Movie>> =
        repo.getAllMovies().asLiveData()

    val watchlist: LiveData<List<Movie>> =
        repo.watchlist.asLiveData()

    fun removeFromWatchlist(id: String) {
        viewModelScope.launch {
            repo.removeFromWatchlist(id)
        }

        fun setWatchlist(id: Int, watch: Boolean) {
            viewModelScope.launch {
                repo.setWatchlisted(id, watch)
            }
        }
    }
}
