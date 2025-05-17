package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    onNavigateToMovieInfo: (String) -> Unit,
    navController: NavHostController,
    movieViewModel: MovieViewModel,
    onNavigateToSearch: () -> Unit = {},
    onNavigateToWatchlist: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val movieState by movieViewModel.movies.collectAsState()

    Scaffold(
        topBar = { HeaderSection() },
        bottomBar = {
            AnimatedNavigationBar(
                navController = navController,
                currentRoute = Screen.Home.route,
                onNavigate = { route -> navController.navigate(route) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Red)
        ) {
            item {
                SearchBar(
                    modifier = Modifier,
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope
                )
            }
            item {
                MovieGrid(movies = movieState, onNavigateToMovieInfo = onNavigateToMovieInfo)
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "TEYATRO",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = Color.Black
        )
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
                        snackbarHostState.showSnackbar("Filter feature is not yet available")
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
fun MovieGrid(
    movies: List<Movie>,
    onNavigateToMovieInfo: (String) -> Unit
) {
    val rowYears = listOf(2020, 2021, 2022, 2023, 2024)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        rowYears.forEach { year ->
            Text(
                text = "$year MMFF Entries",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )

            val rowMovies = movies.filter { it.year == year }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rowMovies) { movie ->
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onNavigateToMovieInfo(movie.id) }
                    ) {
                        val context = LocalContext.current
                        val imageId = remember(movie.imageName) {
                            context.resources.getIdentifier(
                                movie.imageName,
                                "drawable",
                                context.packageName
                            )
                        }

                        if (imageId != 0) {
                            val painter = painterResource(id = imageId)
                            Image(
                                painter = painter,
                                contentDescription = "Movie Poster",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(
                                modifier = Modifier
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
