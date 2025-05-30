package com.example.myapplication

sealed class Screen(val route: String) {
    object Home          : Screen("home")
    object Search        : Screen("search")
    object Watchlist     : Screen("watchlist")
    object Language      : Screen("language")
    object MovieDetails  : Screen("movieDetails/{movieId}") {
        fun createRoute(id: String) = "movieDetails/$id"
    }
}
