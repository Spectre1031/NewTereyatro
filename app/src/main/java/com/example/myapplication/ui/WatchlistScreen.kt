// app/src/main/java/com/example/myapplication/ui/WatchlistScreen.kt
package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.data.Movie
import com.example.myapplication.Screen
import com.example.myapplication.ui.theme.appBackgroundColor
import com.example.myapplication.viewmodel.MovieViewModel
import kotlinx.coroutines.launch

@Composable
fun WatchlistScreen(
    navController: NavHostController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    // 1) get current watchlist as LiveData
    val watchlistMovies by viewModel.watchlist.observeAsState(emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()
    var isEditMode by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = appBackgroundColor(),
        topBar = {
            // same header
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text     = if (isEditMode) "Remove from Watchlist" else "Your Watchlist",
                    fontSize = 20.sp
                )
                IconButton(onClick = { isEditMode = !isEditMode }) {
                    Icon(
                        imageVector        = if (isEditMode) Icons.Default.Check else Icons.Default.Edit,
                        contentDescription = if (isEditMode) "Done" else "Edit",
                        modifier           = Modifier.size(24.dp)
                    )
                }
            }
        },
        bottomBar = {
            AnimatedNavigationBar(
                navController = navController,
                currentRoute  = Screen.Watchlist.route,
                onNavigate    = { route -> navController.navigate(route) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        val screenWidthDp = LocalConfiguration.current.screenWidthDp
        val columns = when {
            screenWidthDp >= 840 -> 4
            screenWidthDp >= 600 -> 3
            else                 -> 2
        }

        LazyVerticalGrid(
            columns               = GridCells.Fixed(columns),
            modifier              = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding        = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement   = Arrangement.spacedBy(16.dp)
        ) {
            items(watchlistMovies, key = { it.movie_id }) { movie ->
                // each poster card
                Card(
                    modifier = Modifier
                        .width((LocalConfiguration.current.screenWidthDp.dp / columns) - 16.dp)
                        .aspectRatio(0.67f)
                        .clickable(enabled = !isEditMode) {
                            navController.navigate(Screen.MovieDetails.createRoute(movie.movie_id))
                        },
                    shape     = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(Modifier.fillMaxSize()) {
                        // load poster
                        val ctx     = LocalContext.current
                        val resId   = ctx.resources.getIdentifier(
                            movie.imageName, "drawable", ctx.packageName
                        )
                        if (resId != 0) {
                            AsyncImage(
                                model               = resId,
                                contentDescription  = movie.title,
                                contentScale        = ContentScale.Crop,
                                modifier            = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No Image", color = Color.DarkGray)
                            }
                        }

                        // remove icon overlay in edit mode
                        if (isEditMode) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        viewModel.removeFromWatchlist(movie)
                                        snackbarHostState.showSnackbar(
                                            "\"${movie.title}\" removed",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(6.dp)
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.7f))
                            ) {
                                Icon(
                                    imageVector        = Icons.Default.Delete,
                                    contentDescription = "Remove",
                                    modifier           = Modifier.size(20.dp),
                                    tint               = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
