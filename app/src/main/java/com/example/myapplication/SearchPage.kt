package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.appBackgroundColor

@Composable
fun SearchPage(
    onBackClick: () -> Unit,
    onNavigateToMovieInfo: (String) -> Unit,
    onNavigateToWatchlist: () -> Unit,
    navController: NavHostController,
    movieViewModel: MovieViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()

    var query        by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf<Int?>(null) }
    var expanded     by remember { mutableStateOf(false) }

    val movieState by movieViewModel.searchResults.collectAsState()

    val allYears = remember {
        MovieRepository.movies
            .map { it.year }
            .distinct()
            .sorted()
    }

    val afterQuery = if (query.isBlank()) movieState
    else movieState.filter { it.title.startsWith(query, ignoreCase = true) }

    val filteredMovies = selectedYear
        ?.let { year -> afterQuery.filter { it.year == year } }
        ?: afterQuery

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar    = {
            AnimatedNavigationBar(
                navController = navController,
                currentRoute  = Screen.Search.route,
                onNavigate    = { route -> navController.navigate(route) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(appBackgroundColor())
                .padding()
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter           = painterResource(R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier        = Modifier.size(30.dp)
                    )
                }
                IconButton(onClick = onNavigateToWatchlist) {
                    Icon(
                        painter           = painterResource(R.drawable.ic_watchlist),
                        contentDescription = "Watchlist",
                        modifier        = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value            = query,
                onValueChange    = { query = it; movieViewModel.performSearch(it) },
                modifier         = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp),
                placeholder      = { Text("Search") },
                leadingIcon      = {
                    Icon(
                        painter           = painterResource(R.drawable.ic_search),
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp),
                    )
                },
                trailingIcon     = {
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(
                                painter           = painterResource(R.drawable.ic_filter),
                                contentDescription = "Filter by Year",
                                modifier = Modifier.size(20.dp),
                            )
                        }
                        DropdownMenu(
                            expanded        = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            // "All years" option
                            DropdownMenuItem(
                                text    = { Text("All years") },
                                onClick = {
                                    selectedYear = null
                                    expanded = false
                                }
                            )
                            // One item per year
                            allYears.forEach { year ->
                                DropdownMenuItem(
                                    text    = { Text(year.toString()) },
                                    onClick = {
                                        selectedYear = year
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                },
                singleLine       = true,
                shape            = RoundedCornerShape(24.dp),
                colors           = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor   = MaterialTheme.colorScheme.primary
                )
            )

            // ── Optional indicator of current filter ────────────
            selectedYear?.let { year ->
                Text(
                    text  = "Showing: $year entries",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            // ── Movie Grid ──────────────────────────────────────
            LazyVerticalGrid(
                columns               = GridCells.Fixed(2),
                contentPadding        = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp),
                modifier              = Modifier.fillMaxSize()
            ) {
                items(filteredMovies) { movie ->
                    Box(
                        Modifier
                            .aspectRatio(0.67f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onNavigateToMovieInfo(movie.id) }
                    ) {
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
                                Modifier
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
