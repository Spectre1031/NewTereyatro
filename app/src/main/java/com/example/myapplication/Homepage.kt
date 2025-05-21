package com.example.myapplication


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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.appBackgroundColor
import kotlinx.coroutines.CoroutineScope

@Composable
fun HomePage(
    onNavigateToMovieInfo: (String) -> Unit,
    navController: NavHostController,
    movieViewModel: MovieViewModel = viewModel(),
    onNavigateToSearch: () -> Unit = {},
    onNavigateToWatchlist: () -> Unit = {}
) {
    val movieState by movieViewModel.searchResults.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope   = rememberCoroutineScope()

    Scaffold(
        containerColor = appBackgroundColor(),
        topBar = { HeaderSection() },
        bottomBar = {
            AnimatedNavigationBar(
                navController  = navController,
                currentRoute   = Screen.Home.route,
                onNavigate     = { route -> navController.navigate(route) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 50.dp)
        ) {
            item {
                SearchBar(
                    modifier           = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 10.dp),
                    snackbarHostState  = snackbarHostState,
                    coroutineScope     = coroutineScope,
                    onQueryChanged     = { movieViewModel.performSearch(it) } // ← hook in
                )
            }
            item {
                MovieGrid(
                    movies                = movieState,
                    onNavigateToMovieInfo = onNavigateToMovieInfo
                )
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text  = "TEYATRO",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold
            ),
        )
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onQueryChanged: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value            = query,
        onValueChange    = { new ->
            query = new
            onQueryChanged(new)
        },
        modifier         = modifier,
        placeholder      = { Text("Search") },
        leadingIcon      = {
            Icon(
                painter           = painterResource(R.drawable.ic_search),
                contentDescription = "Search Icon",
                modifier           = Modifier.size(20.dp)
            )
        },
        singleLine       = true,
        shape            = RoundedCornerShape(25.dp),
        colors           = OutlinedTextFieldDefaults.colors()
    )
}

@Composable
fun MovieGrid(
    movies: List<Movie>,
    onNavigateToMovieInfo: (String) -> Unit
) {
    val rowYears = listOf(2020, 2021, 2022, 2023, 2024)

    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        rowYears.forEach { year ->

            val rowMovies = movies.filter { it.year == year }

            if(rowMovies.isNotEmpty()) {
                Text(
                    text     = "$year MMFF Entries",
                    style    = MaterialTheme.typography.titleMedium.copy(
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )


                LazyRow(
                    modifier            = Modifier.fillMaxWidth(),
                    contentPadding      = PaddingValues(horizontal = 8.dp),
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
                                AsyncImage(
                                    model               = imageId,
                                    contentDescription  = movie.title,
                                    contentScale        = ContentScale.Crop,
                                    modifier            = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    modifier        = Modifier
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
}
