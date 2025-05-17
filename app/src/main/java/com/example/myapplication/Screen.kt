package com.example.myapplication

public sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Watchlist : Screen("watchlist")
    object MovieInfo : Screen("movieinfo")
    object Language : Screen("language")
    object MovieDetails : Screen("movie_details/{movieId}") {
        fun createRoute(movieId: String) = "movie_details/$movieId"
    }
    object Filter : Screen("filter")
}