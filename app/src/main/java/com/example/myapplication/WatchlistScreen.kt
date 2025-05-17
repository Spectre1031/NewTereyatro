package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.grid.items

@Composable
fun WatchlistHeader(
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier.size(30.dp),
                tint = Color.Red
            )
        }

        Text(
            text = "WATCHLIST!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Red
        )

        IconButton(onClick = {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("This feature is not yet available")
            }
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Edit",
                modifier = Modifier.size(24.dp),
                tint = Color.Red
            )
        }
    }
}

@Composable
fun MoviePosterWithInfo(
    movie: Movie,
    movieId: String,
    onNavigateToMovieDetails: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    columns: Int
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val horizontalPadding = 16.dp
    val cardWidth = (screenWidth / columns) - horizontalPadding
    val cardHeight = cardWidth * 1.625f

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .clickable { onNavigateToMovieDetails(movieId) }
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val context = LocalContext.current
            val imageId = remember(movie.imageName) {
                context.resources.getIdentifier(movie.imageName, "drawable", context.packageName)
            }

            if (imageId != 0) {
                val painter = painterResource(id = imageId)
                Image(
                    painter = painter,
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight / 4)
                    .align(Alignment.BottomCenter)
                    .background(Color.White.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_alert),
                    contentDescription = "Info Icon",
                    tint = Color.Red,
                    modifier = Modifier.size(28.dp)
                        .clickable { onNavigateToMovieDetails(movieId) }
                )
            }
        }
    }
}

@Composable
fun WatchlistGrid(
    padding: PaddingValues,
    onNavigateToMovieDetails: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
) {
    val movies = MovieRepository.movies
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    val columns = when {
        screenWidthDp >= 840 -> 4
        screenWidthDp >= 600 -> 3
        else -> 2
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            MoviePosterWithInfo(
                movieId = movie.id,
                movie = movie,
                onNavigateToMovieDetails = onNavigateToMovieDetails,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope,
                columns = columns
            )
        }
    }
}

@Composable
fun WatchlistScreen(
    onBackClick: () -> Unit,
    onNavigateToMovieDetails: (String) -> Unit,
    navController: NavHostController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            WatchlistHeader(
                onBackClick = onBackClick,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            )
        },
        bottomBar = {
            AnimatedNavigationBar(
                navController = navController,
                currentRoute = Screen.Watchlist.route,
                onNavigate = { route -> navController.navigate(route) }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        WatchlistGrid(
            padding = paddingValues,
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState,
            onNavigateToMovieDetails = onNavigateToMovieDetails
        )
    }
}
