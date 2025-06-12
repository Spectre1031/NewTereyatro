package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.data.Movie
import com.example.myapplication.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repo: MovieRepository,
    savedState: SavedStateHandle
    ) : ViewModel() {


    val allMovies: LiveData<List<Movie>> =
        repo.getAllMovies().asLiveData()

    val watchlist: LiveData<List<Movie>> =
        repo.watchlist.asLiveData()

}
