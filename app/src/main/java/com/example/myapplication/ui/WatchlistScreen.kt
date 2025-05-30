package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.Screen
import com.example.myapplication.data.Movie
import com.example.myapplication.ui.theme.appBackgroundColor
import kotlinx.coroutines.launch
import com.example.myapplication.ui.viewmodel.WatchlistViewModel


@Composable
fun WatchlistScreen(
    navController: NavHostController,
    onNavigateToMovieDetails: (String) -> Unit,
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val watchlistMovies by viewModel.watchlistFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isEditMode by remember { mutableStateOf(false) }
    val isDark       = isSystemInDarkTheme()
    val bgColor      = if (isDark) Color.Black else Color.White
    val contentColor = if (isDark) Color.White else Color.Black

    Scaffold(
        containerColor = bgColor,
        contentColor   = contentColor,
        topBar = {
            WatchlistHeader(
                isEditMode = isEditMode,
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
        // compute columns
        val screenWidthDp = LocalConfiguration.current.screenWidthDp
        val columns = when {
            screenWidthDp >= 840 -> 4
            screenWidthDp >= 600 -> 3
            else                 -> 2
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement   = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = watchlistMovies,
                key   = { it.id }
            ) { movie: Movie ->
                MoviePoster(
                    movie                    = movie,
                    isEditMode               = isEditMode,
                    onRemove                 = { removedId ->
                        scope.launch {
                            viewModel.removeFromWatchlist(removedId)
                            snackbarHostState.showSnackbar(
                                message = "Removed “${movie.title}” from watchlist.",
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
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()     // ← adds top padding for the status bar
            .padding(10.dp)
    ) {
        // Centered title
        Text(
            text     = if (isEditMode) "Remove from Watchlist" else "Your Watchlist",
            fontSize = 18.sp,
            color    = textColor,
            modifier = Modifier.align(Alignment.Center)
        )

        // Edit / Done button at the end
        IconButton(
            onClick = onEditModeToggle,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector        = if (isEditMode) Icons.Default.Check else Icons.Default.Edit,
                contentDescription = if (isEditMode) "Done" else "Edit",
                modifier           = Modifier.size(20.dp)
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
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = 16.dp
    val cardWidth  = (screenWidth / columns) - horizontalPadding
    val cardHeight = cardWidth * 1.625f

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .clickable(enabled = !isEditMode) {
                onNavigateToMovieDetails(movie.id.toString())
            },
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
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
                    model              = imageId,
                    contentDescription = movie.title,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier         = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.DarkGray)
                }
            }

            if (isEditMode) {
                IconButton(
                    onClick = { onRemove(movie.id.toString()) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(28.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
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
