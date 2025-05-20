package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) { backStackEntry ->
                            val movieViewModel: MovieViewModel = viewModel(backStackEntry)
                            HomePage(
                                onNavigateToSearch = {
                                    navController.navigate(Screen.Search.route)
                                },
                                onNavigateToWatchlist = {
                                    navController.navigate(Screen.Watchlist.route)
                                },
                                onNavigateToMovieInfo = { movieId ->
                                    val route = Screen.MovieDetails.createRoute(movieId)
                                    Log.d("NavigationTest", "Navigating to: $route")
                                    navController.navigate(route)
                                },
                                navController = navController,
                                movieViewModel = movieViewModel
                            )
                        }

                        composable(Screen.Language.route) {
                            LaunchedEffect(Unit) {
                                snackbarHostState.showSnackbar("This feature is not yet available")
                                navController.popBackStack()
                            }
                        }

                        composable(Screen.Filter.route) {
                            LaunchedEffect(Unit) {
                                snackbarHostState.showSnackbar("This feature is not yet available")
                                navController.popBackStack()
                            }
                        }
                        composable(Screen.Search.route) { backStackEntry ->
                            val movieViewModel: MovieViewModel = viewModel(backStackEntry)

                            SearchPage(
                                onBackClick = { navController.popBackStack() },
                                onNavigateToMovieInfo = { movieId ->
                                    val route = Screen.MovieDetails.createRoute(movieId)
                                    navController.navigate(route)
                                },
                                onNavigateToWatchlist = {
                                    navController.navigate(Screen.Watchlist.route)
                                },
                                navController = navController,
                                movieViewModel = movieViewModel
                            )
                        }

                        composable(Screen.Watchlist.route) {
                            WatchlistScreen(
                                onBackClick = { navController.popBackStack() },
                                onNavigateToMovieDetails = { movieId ->
                                    val route = Screen.MovieDetails.createRoute(movieId)
                                    Log.d("NavigationTest", "Navigating to: $route")
                                    navController.navigate(route)
                                },
                                navController = navController
                            )
                        }

                        composable(
                            route = Screen.MovieDetails.route,
                            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getString("movieId") ?: "unknown"
                            Log.d("NavigationTest", "Opened MovieDetailsPage with movieId: $movieId")
                            MovieDetailsPage(
                                movieId = movieId,
                                onBackClick = { navController.popBackStack() },
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
