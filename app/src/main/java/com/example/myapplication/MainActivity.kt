package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import com.example.myapplication.ui.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                // ── Hoist your language state ──
                var currentLanguage by rememberSaveable { mutableStateOf("en") }

                NavHost(
                    navController    = navController,
                    startDestination = Screen.Home.route,
                    modifier         = Modifier.padding()
                ) {
                    // Home
                    composable(Screen.Home.route) {
                        HomePage(
                            navController    = navController,
                            currentLanguage  = currentLanguage,
                            onLanguageChange = { currentLanguage = it }
                        )
                    }

                    // Search
                    composable(Screen.Search.route) {
                        SearchPage(
                            navController    = navController,
                            currentLanguage  = currentLanguage,
                            onLanguageChange = { currentLanguage = it }
                        )
                    }

                    // Watchlist
                    composable(Screen.Watchlist.route) {
                        WatchlistScreen(
                            navController            = navController,
                            currentLanguage          = currentLanguage,
                            onLanguageChange         = { currentLanguage = it },
                            onNavigateToMovieDetails = { movieId ->
                                // ➌ Use the same createRoute() helper:
                                navController.navigate(
                                    Screen.MovieDetails.createRoute(movieId)
                                )
                            }
                        )
                    }

                    // Movie Details
                    composable(
                        route     = Screen.MovieDetails.routeWithArg,      // must match exactly
                        arguments = listOf(navArgument("movieId") {
                            type = NavType.IntType
                        })
                    ) {
                        MovieDetailsPage(
                            navController    = navController,
                            currentLanguage  = currentLanguage,
                            onLanguageChange = { currentLanguage = it },
                            onBackClick      = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
