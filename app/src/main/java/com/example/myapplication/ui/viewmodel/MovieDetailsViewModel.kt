// app/src/main/java/com/example/myapplication/ui/viewmodel/MovieDetailsViewModel.kt

package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Movie
import com.example.myapplication.data.MovieRepository
import com.example.myapplication.data.TranslationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repo: MovieRepository,
    private val translationRepo: TranslationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"]) {
        "MovieDetailsViewModel: missing movieId"
    }

    val movieFlow: StateFlow<Movie?> = repo
        .getMovieById(movieId)
        .stateIn(
            scope       = viewModelScope,
            started     = SharingStarted.Lazily,
            initialValue= null
        )

    private val _synopsis = MutableStateFlow<String>("")
    val synopsis: StateFlow<String> = _synopsis.asStateFlow()

    fun loadSynopsis(targetLang: String) {
        viewModelScope.launch { 
            val original = movieFlow.value?.description.orEmpty()

            _synopsis.value = if (targetLang == "en") {
                original
            } else {
                translationRepo.translate(original, targetLang)
            }
        }
    }

    fun addToWatchlist() {
        viewModelScope.launch {
            movieFlow.value?.let { repo.addToWatchlist(it) }
        }
    }
}
