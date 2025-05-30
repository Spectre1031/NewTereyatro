package com.example.myapplication.ui

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Divider
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.Screen
import com.example.myapplication.ui.viewmodel.MovieViewModel

@Composable
fun SearchPage(
    navController: NavHostController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    // 1. Get your movie list (LiveData → Compose state)
    val movies by viewModel.allMovies.observeAsState(emptyList())

    // 2. Local UI state
    var query         by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf<String?>(null) }
    var selectedYear  by remember { mutableStateOf<Int?>(null) }

    // 3. Build distinct filter lists
    val genres = remember(movies) {
        movies
            .flatMap { listOf(it.genre_one, it.genre_two, it.genre_three) }
            .distinct()
            .sorted()
    }
    val years = remember(movies) {
        movies.map { it.year }.distinct().sorted()
    }

    // 4. Colors for dark/light
    val isDark       = isSystemInDarkTheme()
    val bgColor      = if (isDark) Color.Black else Color.White
    val contentColor = if (isDark) Color.White else Color.Black
    val menuBgColor  = if (isDark) Color.Black else Color.White
    val menuTextColor= if (isDark) Color.White else Color.Black
    val dropDownColor= if (isDark) Color.DarkGray else Color.White


    Scaffold(
        containerColor = bgColor,
        contentColor   = contentColor,
        bottomBar = {
            AnimatedNavigationBar(
                navController  = navController,
                currentRoute   = Screen.Search.route,
                onNavigate     = { route -> navController.navigate(route) }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // ── Search field (no trailingIcon) ───────────────────────
            OutlinedTextField(
                value         = query,
                onValueChange = { query = it },
                placeholder   = { Text("Search…", color = contentColor.copy(alpha = 0.6f)) },
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(24.dp),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor    = bgColor,
                    unfocusedContainerColor  = bgColor,
                    focusedBorderColor       = contentColor,
                    unfocusedBorderColor     = contentColor.copy(alpha = 0.6f),
                    cursorColor              = contentColor
                )
            )

            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Genre button
                var expandedGenre by remember { mutableStateOf(false) }
                Box(Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { expandedGenre = true },
                        shape   = RoundedCornerShape(24.dp),
                        modifier= Modifier
                            .fillMaxWidth(),
                        colors  = ButtonDefaults.outlinedButtonColors(
                            containerColor = dropDownColor,
                            contentColor   = menuTextColor
                        )
                    ) {
                        Text(selectedGenre ?: "Movies")
                        Icon(
                            imageVector       = Icons.Filled.ArrowDropDown,
                            contentDescription= null,
                            tint              = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier          = Modifier.size(20.dp)
                        )
                    }
                    DropdownMenu(
                        expanded         = expandedGenre,
                        onDismissRequest = { expandedGenre = false },
                        modifier         = Modifier
                            .background(menuBgColor)
                            .heightIn(max = 300.dp)
                    ) {
                        DropdownMenuItem(
                            text    = { Text("All Movies", color = menuTextColor) },
                            onClick = {
                                selectedGenre = null
                                selectedYear  = null
                                expandedGenre = false
                            }
                        )
                        genres.forEach { genre ->
                            DropdownMenuItem(
                                text    = { Text(genre, color = menuTextColor) },
                                onClick = {
                                    selectedGenre = genre
                                    expandedGenre = false
                                }
                            )
                        }
                    }
                }

                // Year button
                var expandedYear by remember { mutableStateOf(false) }
                Box(Modifier.weight(1f)) {
                    FilledTonalButton(
                        onClick  = { expandedYear = true },
                        shape    = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors   = ButtonDefaults.filledTonalButtonColors(
                            containerColor = dropDownColor,
                            contentColor   = menuTextColor
                        )
                    ) {
                        Text(selectedYear?.toString() ?: "Year")
                        Icon(
                            imageVector       = Icons.Filled.ArrowDropDown,
                            contentDescription= null,
                            tint              = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier          = Modifier.size(20.dp)
                        )
                    }
                    DropdownMenu(
                        expanded         = expandedYear,
                        onDismissRequest = { expandedYear = false },
                        modifier         = Modifier
                            .background(menuBgColor)
                            .heightIn(max = 300.dp)
                    ) {
                        DropdownMenuItem(
                            text    = { Text("All Years", color = menuTextColor) },
                            onClick = {
                                selectedYear = null
                                selectedGenre = null
                                expandedYear = false
                            }
                        )
                        years.forEach { y ->
                            DropdownMenuItem(
                                text    = { Text(y.toString(), color = menuTextColor    ) },
                                onClick = {
                                    selectedYear  = y
                                    expandedYear  = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Active filter label ──────────────────────────────────
            if (selectedGenre != null || selectedYear != null) {
                Text(
                    text = buildString {
                        selectedGenre?.let { append("Genre: $it  ") }
                        selectedYear?. let { append("Year: $it") }
                    },
                    color    = contentColor.copy(alpha = 0.7f),
                    style    = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // ── Filter + grid ───────────────────────────────────────
            val filtered = movies
                .filter { m -> query.isBlank() || m.title.contains(query, true) }
                .filter { m -> selectedGenre == null ||
                        listOf(m.genre_one, m.genre_two, m.genre_three)
                            .contains(selectedGenre) }
                .filter { m -> selectedYear == null || m.year == selectedYear }

            LazyVerticalGrid(
                columns               = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp),
                contentPadding        = PaddingValues(8.dp),
                modifier              = Modifier.fillMaxSize()
            ) {
                items(filtered) { movie ->
                    Box(
                        Modifier
                            .aspectRatio(0.67f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                navController.navigate(
                                    Screen.MovieDetails.createRoute(movie.id.toString())
                                )
                            }
                    ) {
                        val ctx = LocalContext.current
                        val res = remember(movie.imageName) {
                            ctx.resources.getIdentifier(movie.imageName, "drawable", ctx.packageName)
                        }
                        if (res != 0) {
                            AsyncImage(
                                model              = res,
                                contentDescription = movie.title,
                                contentScale       = ContentScale.Crop,
                                modifier           = Modifier.fillMaxSize()
                            )
                        }  else {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text  = "No Image",
                            color = Color.DarkGray
                        )
                    }
                }
                        if (movie.isWatchlisted) {
                            Icon(
                                imageVector        = Icons.Filled.Star,
                                contentDescription = "Watchlisted",
                                tint               = MaterialTheme.colorScheme.primary,
                                modifier           = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
