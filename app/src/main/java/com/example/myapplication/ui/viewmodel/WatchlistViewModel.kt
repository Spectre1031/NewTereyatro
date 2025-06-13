package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val watchlistFlow = repo.watchlistFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun removeFromWatchlist(id: String) {
        viewModelScope.launch {
            repo.removeFromWatchlist(id)
        }
    }
}
