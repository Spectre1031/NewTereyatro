package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.Movie
import com.example.myapplication.viewmodel.MovieViewModel

@Composable
fun MovieInfoPage(
    movieId: Int,
    viewModel: MovieViewModel = hiltViewModel()
) {
    // Observe a single Movie via LiveData
    val movie by viewModel.movieById(movieId).observeAsState(initial = null)

    if (movie == null) {
        Text("Loading…", Modifier.padding(16.dp))
    } else {
        val m: Movie = movie!!
        Column(Modifier.padding(16.dp)) {
            Text(m.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("Genres: ${m.genre_one}, ${m.genre_two}, ${m.genre_three}")
            Spacer(Modifier.height(8.dp))
            Text(m.description)
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                if (m.isWatchlisted) viewModel.removeFromWatchlist(m)
                else viewModel.addToWatchlist(m)
            }) {
                Text(if (m.isWatchlisted) "Remove from Watchlist" else "Add to Watchlist")
            }
        }
    }
}
