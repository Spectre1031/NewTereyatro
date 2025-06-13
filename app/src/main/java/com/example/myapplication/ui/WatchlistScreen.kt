package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.AnimatedNavigationBar
import com.example.myapplication.Screen
import com.example.myapplication.data.Movie
import com.example.myapplication.ui.viewmodel.WatchlistViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    navController: NavHostController,
    onNavigateToMovieDetails: (String) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val watchlistMovies    by viewModel.watchlistFlow.collectAsState()
    val snackbarHostState  = remember { SnackbarHostState() }
    val scope              = rememberCoroutineScope()
    var isEditMode         by remember { mutableStateOf(false) }
    var pendingRemoveId    by remember { mutableStateOf<String?>(null) }

    val isDark       = isSystemInDarkTheme()
    val bgColor      = if (isDark) Color.Black else Color.White
    val contentColor = if (isDark) Color.White else Color.Black
    val popUp = if (isDark) Color.White else Color.Gray

    Scaffold(
        containerColor = bgColor,
        contentColor   = contentColor,
        topBar = {
            WatchlistHeader(
                isEditMode        = isEditMode,
                onEditModeToggle  = { isEditMode = !isEditMode }
            )
        },
        bottomBar = {
            AnimatedNavigationBar(
                navController     = navController,
                currentRoute      = Screen.Watchlist.route,
                currentLanguage   = currentLanguage,
                onNavigate        = { route -> navController.navigate(route) },
                onLanguageChange  = onLanguageChange
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
            columns             = GridCells.Fixed(columns),
            modifier            = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding      = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement   = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = watchlistMovies,
                key   = { it.id }
            ) { movie ->
                MoviePoster(
                    movie                    = movie,
                    isEditMode               = isEditMode,
                    onRemove                 = { pendingRemoveId = it },
                    onNavigateToMovieDetails = onNavigateToMovieDetails,
                    columns                  = columns
                )
            }
        }

        pendingRemoveId?.let { movieId ->
            AlertDialog(
                onDismissRequest = { pendingRemoveId = null },
                containerColor   = bgColor,
                title            = { Text("Remove from Watchlist", color = contentColor) },
                text             = { Text("Are you sure you want to remove this movie from your watchlist?", color = contentColor) },
                confirmButton    = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                viewModel.removeFromWatchlist(movieId)
                                snackbarHostState.showSnackbar(
                                    "Removed from watchlist.",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            pendingRemoveId = null
                        }
                    ) { Text("Yes", color = contentColor) }
                },
                dismissButton    = {
                    TextButton(onClick = { pendingRemoveId = null }) { Text("No", color = contentColor) }
                }
            )
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
            .statusBarsPadding()
            .padding(10.dp)
    ) {
        Text(
            text     = if (isEditMode) "Remove from Watchlist" else "Your Watchlist",
            fontSize = 18.sp,
            color    = textColor,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick  = onEditModeToggle,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector        = if (isEditMode) Icons.Default.Check else Icons.Default.Edit,
                contentDescription = if (isEditMode) "Done" else "Edit",
                modifier           = Modifier.size(20.dp),
                tint               = textColor
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
    val screenWidth   = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPad = 16.dp
    val cardWidth     = (screenWidth / columns) - horizontalPad
    val cardHeight    = cardWidth * 1.625f

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
                ctx.resources.getIdentifier(movie.imageName, "drawable", ctx.packageName)
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
                    Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.DarkGray)
                }
            }

            if (isEditMode) {
                IconButton(
                    onClick    = { onRemove(movie.id.toString()) },
                    modifier   = Modifier
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
