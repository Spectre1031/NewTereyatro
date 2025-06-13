package com.example.myapplication

sealed class Screen(val route: String) {
    object Home         : Screen("home")
    object Search       : Screen("search")
    object Watchlist    : Screen("watchlist")
    object MovieDetails : Screen("movieDetails") {

        const val routeWithArg = "movieDetails/{movieId}"

        fun createRoute(movieId: String) = "movieDetails/$movieId"
    }
}
