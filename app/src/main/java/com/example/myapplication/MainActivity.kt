package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.myapplication.ui.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backStack    by navController.currentBackStackEntryAsState()
            val currentRoute = backStack?.destination?.route ?: Screen.Home.route

            MaterialTheme {
                    NavHost(
                        navController    = navController,
                        startDestination = Screen.Home.route,
                        modifier         = Modifier.padding()
                    ) {
                        // Home
                        composable(Screen.Home.route) {
                            HomePage(navController)
                        }
                        // Search
                        composable(Screen.Search.route) {
                            SearchPage(navController)
                        }
                        // Watchlist
                        composable(Screen.Watchlist.route) {
                            WatchlistScreen(
                                navController             = navController,
                                onNavigateToMovieDetails = { movieId ->
                                    navController.navigate(Screen.MovieDetails.createRoute(movieId.toInt()
                                        .toString()))
                                }
                            )
                        }


                        composable(
                            route = Screen.MovieDetails.route,            // e.g. "movieDetails/{movieId}"
                            arguments = listOf(navArgument("movieId") {
                                type = NavType.IntType
                            })
                        ) {
                            // THIS will instantiate MovieDetailsViewModel and supply 'movieId'
                            MovieDetailsPage(
                                navController  = navController,
                                onBackClick    = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
