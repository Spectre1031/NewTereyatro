package com.example.myapplication

import com.example.myapplication.Movie
import com.example.myapplication.MovieRepository
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.myapplication.HomePage



@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onNavigateToMovieInfo: (String) -> Unit,
    onNavigateToWatchlist: () -> Unit,
    navController: NavHostController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AnimatedNavigationBar(
                navController = navController,
                currentRoute = Screen.Search.route,
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            SearchHeader(
                onBackClick = onBackClick,
                onNavigateToWatchlist = onNavigateToWatchlist
            )
            Spacer(modifier = Modifier.height(10.dp))

            SearchBar(
                modifier = Modifier.height(50.dp),
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            )

            Spacer(modifier = Modifier.height(10.dp))
            val randomMovies = remember {
                MovieRepository.movies.shuffled().take(12)
            }

            SearchContent(
                movies = randomMovies,
                onPosterClick = { movieId ->
                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                }
            )
        }
    }
}





@Composable
private fun SearchHeader(onBackClick: () -> Unit, onNavigateToWatchlist: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp),
                tint = Color.Red
            )
        }

        IconButton(
            onClick = onNavigateToWatchlist,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_watchlist),
                contentDescription = "Watchlist",
                modifier = Modifier.size(25.dp),
                tint = Color.Red
            )
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 10.dp),

        placeholder = {
            Text(
                "Search",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search Icon",
                tint = Color.Red,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Filter feature is not available yet")
                    }
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = "Filter",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(25.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Black,
            focusedBorderColor = Color.Black
        )
    )
}


@Composable
private fun SearchContent(movies: List<Movie>, onPosterClick: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Have You Seen This Movie?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 140.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        )
        {
            items(movies) { movie ->
                MoviePoster(
                    movie = movie,
                    onClick = { onPosterClick(movie.id) }
                )
            }

        }
    }
}


@Composable
private fun MoviePoster(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.67f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val context = LocalContext.current
            val imageId = remember(movie.imageName) {
                context.resources.getIdentifier(movie.imageName, "drawable", context.packageName)
            }
            val painter = painterResource(id = imageId)

            Image(
                painter = painter,
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
