package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.Screen
import com.example.myapplication.data.Movie
import com.example.myapplication.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val isDark = isSystemInDarkTheme()

    // White in dark mode, black in light mode
    val bgColor      = if (isDark) Color.Black else Color.White
    // Contrast text/icons: black in dark, white in light
    val contentColor = if (isDark) Color.White else Color.Black

    val movies by viewModel.allMovies.observeAsState(initial = emptyList())
    var query by remember { mutableStateOf("") }

    Scaffold(
        containerColor = bgColor,
        contentColor   = contentColor,
        bottomBar = {
            AnimatedNavigationBar(
                navController = navController,
                currentRoute  = Screen.Home.route,
                onNavigate    = { route -> navController.navigate(route) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)           // also paint the list background
                .padding(innerPadding)
        ) {
            item {
                Text(
                    "TEYATRO",
                    color    = contentColor,
                    style    = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search", color = contentColor.copy(alpha = 0.6f)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textStyle = LocalTextStyle.current.copy(color = contentColor),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor    = bgColor,
                        unfocusedContainerColor  = bgColor,
                        focusedBorderColor       = contentColor,
                        unfocusedBorderColor     = contentColor.copy(alpha = 0.6f),
                        cursorColor              = contentColor,
                        errorCursorColor         = MaterialTheme.colorScheme.error,
                        focusedLabelColor        = contentColor,
                        unfocusedLabelColor      = contentColor.copy(alpha = 0.6f),
                        focusedLeadingIconColor  = contentColor,
                        unfocusedLeadingIconColor= contentColor.copy(alpha = 0.6f),
                        focusedTrailingIconColor = contentColor,
                        unfocusedTrailingIconColor= contentColor.copy(alpha = 0.6f),
                    ),
                    shape = RoundedCornerShape(25.dp)
                )
            }

            item {
                MovieGrid(
                    movies     = movies.filter { query.isBlank() || it.title.contains(query, ignoreCase = true) },
                    navController = navController,
                    viewModel     = viewModel
                )
            }
        }
    }
}

@Composable
private fun MovieGrid(
    movies: List<Movie>,
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val years by viewModel.yearsFlow.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        years.forEach { year ->
            val row = movies.filter { it.year == year }
            if (row.isNotEmpty()) {
                Text(
                    "$year MMFF Entries",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(row) { movie ->
                        Box(
                            modifier = Modifier
                                .width(140.dp)
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    navController.navigate(Screen.MovieDetails.createRoute(movie.id.toString()))
                                }
                        ) {
                            val ctx = LocalContext.current
                            val res = remember(movie.imageName) {
                                ctx.resources.getIdentifier(movie.imageName, "drawable", ctx.packageName)
                            }
                            if (res != 0) {
                                AsyncImage(
                                    model = res,
                                    contentDescription = movie.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
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

                            // watchlist toggle
                            if (movie.isWatchlisted) {
                                IconButton(
                                    onClick = {
                                        viewModel.removeFromWatchlist(movie)
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector        = Icons.Filled.Star,
                                        contentDescription = "Watchlisted",
                                        tint               = MaterialTheme.colorScheme.primary,
                                        modifier           = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
