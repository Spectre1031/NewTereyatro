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

    // 1) grab movieId from nav args
    private val movieId: Int = checkNotNull(savedStateHandle["movieId"]) {
        "MovieDetailsViewModel: missing movieId"
    }

    // 2) stream the Movie? from your DAO
    val movieFlow: StateFlow<Movie?> = repo
        .getMovieById(movieId)
        .stateIn(
            scope       = viewModelScope,
            started     = SharingStarted.Lazily,
            initialValue= null
        )

    // 3) hold the translated synopsis
    private val _synopsis = MutableStateFlow<String>("")
    val synopsis: StateFlow<String> = _synopsis.asStateFlow()

    /**
     * 4) Call this whenever the user flips the language toggle.
     *    It fetches the original synopsis (in English) then,
     *    if needed, translates to Tagalog via LibreTranslate.
     */
    fun loadSynopsis(targetLang: String) {
        viewModelScope.launch {
            // original text (or empty)
            val original = movieFlow.value?.description.orEmpty()

            _synopsis.value = if (targetLang == "en") {
                original
            } else {
                translationRepo.translate(original, targetLang)
            }
        }
    }

    /**
     * 5) Add the current movie to watchlist.
     *    Called by your top‐bar “+” button.
     */
    fun addToWatchlist() {
        viewModelScope.launch {
            movieFlow.value?.let { repo.addToWatchlist(it) }
        }
    }
}
