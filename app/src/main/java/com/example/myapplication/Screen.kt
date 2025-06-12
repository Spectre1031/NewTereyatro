package com.example.myapplication

sealed class Screen(val route: String) {
    object Home         : Screen("home")
    object Search       : Screen("search")
    object Watchlist    : Screen("watchlist")
    object MovieDetails : Screen("movieDetails") {
        // the pattern our NavHost will listen to:
        const val routeWithArg = "movieDetails/{movieId}"

        // helper for building the actual route you’ll pass to navigate():
        fun createRoute(movieId: String) = "movieDetails/$movieId"
    }
}
