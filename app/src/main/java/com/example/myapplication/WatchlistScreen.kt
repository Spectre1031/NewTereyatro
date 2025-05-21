package com.example.myapplication

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.appBackgroundColor
import kotlinx.coroutines.launch

@Composable
fun WatchlistScreen(
    onNavigateToMovieDetails: (String) -> Unit,
    navController: NavHostController
) {
    val watchlistMovies by MovieRepository.watchlistFlow.collectAsState(initial = emptyList())

    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()

    var isEditMode by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = appBackgroundColor(),
        topBar = {
            WatchlistHeader(
                isEditMode       = isEditMode,
                onEditModeToggle = { isEditMode = !isEditMode }
            )
        },
        bottomBar = {
            AnimatedNavigationBar(
                navController  = navController,
                currentRoute   = Screen.Watchlist.route,
                onNavigate     = { route -> navController.navigate(route) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        // determine column count based on screen width
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
            items(watchlistMovies, key = { it.id }) { movie ->
                MoviePoster(
                    movie                    = movie,
                    isEditMode               = isEditMode,
                    onRemove                 = { removedId ->
                        scope.launch {
                            MovieRepository.removeFromWatchlist(removedId)
                            snackbarHostState.showSnackbar(
                                message = "Removed \"${movie.title}\" from watchlist.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    onNavigateToMovieDetails = onNavigateToMovieDetails,
                    columns                  = columns
                )
            }
        }
    }
}

@Composable
private fun WatchlistHeader(
    isEditMode: Boolean,
    onEditModeToggle: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text     = if (isEditMode) "Remove from Watchlist" else "Your Watchlist",
            fontSize = 18.sp,
        )

        IconButton(onClick = onEditModeToggle) {
            Icon(
                imageVector       = if (isEditMode) Icons.Default.Check else Icons.Default.Edit,
                contentDescription = if (isEditMode) "Done" else "Edit",
                modifier          = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun MoviePoster(
    movie: Movie,
    isEditMode: Boolean,
    onRemove: (String) -> Unit,
    onNavigateToMovieDetails: (String) -> Unit,
    columns: Int
) {
    // calculate card size
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = 16.dp
    val cardWidth  = (screenWidth / columns) - horizontalPadding
    val cardHeight = cardWidth * 1.625f

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .clickable(enabled = !isEditMode) {
                onNavigateToMovieDetails(movie.id)
            },
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            // poster image
            val ctx     = LocalContext.current
            val imageId = remember(movie.imageName) {
                ctx.resources.getIdentifier(
                    movie.imageName,
                    "drawable",
                    ctx.packageName
                )
            }

            if (imageId != 0) {
                AsyncImage(
                    model               = imageId,
                    contentDescription  = movie.title,
                    contentScale        = ContentScale.Crop,
                    modifier            = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.DarkGray)
                }
            }

            // delete icon overlay when in edit mode
            if (isEditMode) {
                IconButton(
                    onClick = { onRemove(movie.id) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(28.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(
                        imageVector       = Icons.Default.Delete,
                        contentDescription = "Remove",
                        modifier          = Modifier.size(20.dp),
                        tint               = Color.Red
                    )
                }
            }
        }
    }
}
