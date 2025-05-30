package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.filled.Star
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
fun SearchPage(
    navController: NavHostController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val movies by viewModel.allMovies.observeAsState(initial = emptyList())
    var query by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            AnimatedNavigationBar(
                navController = navController,
                currentRoute  = Screen.Search.route,
                onNavigate    = { route -> navController.navigate(route) }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(Modifier.height(8.dp))

            val filtered = movies.filter { m: Movie ->
                query.isBlank() || m.title.contains(query, ignoreCase = true)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered) { movie ->
                    Box(
                        Modifier
                            .aspectRatio(0.67f)
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
                    }
                }
            }
        }
    }
}
