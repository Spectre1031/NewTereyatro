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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.AnimatedNavigationBar
import com.example.myapplication.MovieRepository
import com.example.myapplication.Movie
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext


@Composable
fun MovieDetailsPage(navController: NavHostController, movieId: String, onBackClick: () -> Unit = {}) {
    val movie = remember(movieId) {
        MovieRepository.movies.find { it.id == movieId }
    }

    Scaffold(
        bottomBar = {
            AnimatedNavigationBar(
                navController = navController,
                currentRoute = Screen.MovieDetails.route,
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .padding(padding)
        ) {
            item { Header(onBackClick) }
            item { MediaSection(movie) }
            item { ActionButtonsSection() }
            item { SynopsisSection(movie) }
            item { ActorsSection() }
            item {
                PostersByDatesSection { movieId ->
                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun Header(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.8f), CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.Red,
                modifier = Modifier.size(30.dp)
            )
        }

        IconButton(
            onClick = { },
            modifier = Modifier
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.8f), CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_watchlist),
                contentDescription = "Add to Watchlist",
                tint = Color.Red,
                modifier = Modifier.size(30.dp)
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
                context.resources.getIdentifier(it.imageName, "drawable", context.packageName)
            } ?: R.drawable.bignight
        }
        val painter = painterResource(id = imageId)

        Image(
            painter = painter,
            contentDescription = "Movie Media",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
                .background(Color.Black.copy(alpha = 0.8f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
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
            color = Color.Black,
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
                color = Color.Black,
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
                color = Color.Black,
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