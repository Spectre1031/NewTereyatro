package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.appBackgroundColor
import kotlinx.coroutines.launch

@Composable
fun MovieDetailsPage(
    navController: NavHostController,
    movieId: String,
    onBackClick: () -> Unit = {}
) {
    val movie = remember(movieId) {
        MovieRepository.movies.find { it.id == movieId }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope    = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar    = {
            AnimatedNavigationBar(
                navController  = navController,
                currentRoute   = Screen.MovieDetails.route,
                onNavigate     = { route -> navController.navigate(route) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(appBackgroundColor())
                .padding(horizontal = 16.dp)
                .padding()
        ) {
            item {
                Header(
                    onBackClick = onBackClick,
                    onAddToWatchlist = {
                        movie?.let { m ->
                            coroutineScope.launch {
                                MovieRepository.addToWatchlist(m.id)
                                snackbarHostState.showSnackbar(
                                    "${m.title} added to watchlist"
                                )
                            }
                        }
                    }
                )
            }
            item { MediaSection(movie) }
            item { ActionButtonsSection() }
            item { SynopsisSection(movie) }
            item { ActorsSection() }
            item {
                PostersByDatesSection { id ->
                    navController.navigate(Screen.MovieDetails.createRoute(id))
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

private fun SnackbarHostState.showSnackbar(text: String) {}

@Composable
private fun Header(
    onBackClick: () -> Unit,
    onAddToWatchlist: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        // Back button (unchanged)
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                painter           = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier           = Modifier.size(30.dp)
            )
        }

        // Add-to-Watchlist button (shows Snackbar on tap)
        IconButton(
            onClick = onAddToWatchlist,
            modifier = Modifier
                .size(40.dp)
        ) {
            Icon(
                painter           = painterResource(id = R.drawable.ic_watchlist),
                contentDescription = "Save to Watchlist",
                modifier           = Modifier.size(30.dp)
            )
        }
    }
}


@Composable
fun MediaSection(movie: Movie?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        val context = LocalContext.current
        val imageId = remember(movie?.imageName) {
            movie?.let {
                context.resources.getIdentifier(
                    it.imageName, "drawable", context.packageName
                )
            } ?: R.drawable.bignight
        }

        AsyncImage(
            model               = imageId,
            contentDescription  = movie?.title.orEmpty(),
            contentScale        = ContentScale.Crop,
            modifier            = Modifier.fillMaxSize()
        )

        // gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        // play button
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter           = painterResource(id = R.drawable.ic_play),
                contentDescription = "Play",
                tint               = Color.White,
                modifier           = Modifier.size(32.dp)
            )
        }
    }
}



@Composable
fun ActionButtonsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        GenreButton("Action")
        Spacer(modifier = Modifier.width(8.dp))
        GenreButton("Drama")
        Spacer(modifier = Modifier.width(8.dp))
        GenreButton("Comedy")
    }
}

@Composable
private fun GenreButton(text: String) {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1E1E1E),
            contentColor = Color.White
        ),
        modifier = Modifier.height(36.dp),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

@Composable
fun SynopsisSection(movie: Movie?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = movie?.title ?: "Unknown Title",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = movie?.synopsis ?: "No synopsis available.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ActorsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Actors",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(5) { index ->
                ActorCard(index)
            }
        }
    }
}

@Composable
private fun ActorCard(index: Int) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(160.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                val painter = painterResource(id = R.drawable.bignight)

                Image(
                    painter = painter,
                    contentDescription = "Actor ${index + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun PostersByDatesSection(onPosterClick: (String) -> Unit) {
    val movies2022 = MovieRepository.movies.filter { it.year == 2022 }
    val movies2023 = MovieRepository.movies.filter { it.year == 2023 }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        if (movies2022.isNotEmpty()) {
            Text(
                text = "2022 Entries",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(movies2022) { movieItem ->
                    Card(
                        modifier = Modifier
                            .width(140.dp)
                            .aspectRatio(0.67f)
                            .clickable { onPosterClick(movieItem.id) },
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        val context = LocalContext.current
                        val imageId = remember(movieItem.imageName) {
                            context.resources.getIdentifier(
                                movieItem.imageName,
                                "drawable",
                                context.packageName
                            )
                        }
                        val painter = painterResource(id = imageId)

                        Image(
                            painter = painter,
                            contentDescription = movieItem.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        if (movies2023.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "2023 Entries",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(movies2023) { movieItem ->
                    Card(
                        modifier = Modifier
                            .width(140.dp)
                            .aspectRatio(0.67f)
                            .clickable { onPosterClick(movieItem.id) },
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        val context = LocalContext.current
                        val imageId = remember(movieItem.imageName) {
                            context.resources.getIdentifier(
                                movieItem.imageName,
                                "drawable",
                                context.packageName
                            )
                        }
                        val painter = painterResource(id = imageId)

                        Image(
                            painter = painter,
                            contentDescription = movieItem.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}