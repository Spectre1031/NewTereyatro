package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.Screen
import com.example.myapplication.data.Movie
import com.example.myapplication.viewmodel.MovieViewModel

@Composable
fun HomePage(
    navController: NavHostController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val movies      by viewModel.allMovies.observeAsState(emptyList())
    var query by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            AnimatedNavigationBar(
                navController = navController,
                currentRoute  = Screen.Home.route,
                onNavigate    = { route -> navController.navigate(route) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Text(
                    "TEYATRO",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(25.dp)
                )
            }
            item {
                MovieGrid(
                    movies = movies.filter {
                        query.isBlank() || it.title.contains(query, ignoreCase = true)
                    },
                    navController = navController,
                    viewModel     = viewModel
                )
            }
        }
    }
}

@Composable
private fun MovieGrid(
    movies: List<Movie>,
    navController: NavHostController,
    viewModel: MovieViewModel
) {
    val years = listOf(2020, 2021, 2022, 2023, 2024)
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        years.forEach { year ->
            val row = movies.filter { it.year == year }
            if (row.isNotEmpty()) {
                Text(
                    "$year MMFF Entries",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(row) { movie ->
                        Box(
                            modifier = Modifier
                                .width(140.dp)
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    navController.navigate(Screen.MovieDetails.createRoute(movie.movie_id))
                                }
                        ) {
                            val ctx = LocalContext.current
                            val res = remember(movie.imageName) {
                                ctx.resources.getIdentifier(movie.imageName, "drawable", ctx.packageName)
                            }
                            if (res != 0) {
                                AsyncImage(
                                    model = res,
                                    contentDescription = movie.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(Color.Gray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No Image", color = Color.White)
                                }
                            }

                            // watchlist toggle
                            IconButton(
                                onClick = {
                                    if (movie.isWatchlisted) viewModel.removeFromWatchlist(movie)
                                    else viewModel.addToWatchlist(movie)
                                },
                                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                            ) {
                                Icon(
                                    imageVector =
                                        if (movie.isWatchlisted) Icons.Filled.Star
                                        else Icons.Outlined.StarOutline,
                                    contentDescription = null,
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
