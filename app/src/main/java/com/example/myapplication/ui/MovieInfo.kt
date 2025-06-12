package com.example.myapplication.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.AnimatedNavigationBar
import com.example.myapplication.R
import com.example.myapplication.Screen
import com.example.myapplication.data.Movie
import com.example.myapplication.ui.viewmodel.MovieDetailsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsPage(
    navController: NavHostController,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    onBackClick: () -> Unit = { navController.popBackStack() },
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    // ❶ Collect movie and translated synopsis
    val movie    by viewModel.movieFlow.collectAsState()
    val synopsis by viewModel.synopsis.collectAsState()

    // ❷ Reload synopsis when movie loads or language changes
    LaunchedEffect(movie, currentLanguage) {
        movie?.let { viewModel.loadSynopsis(currentLanguage) }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope             = rememberCoroutineScope()
    val uriHandler        = LocalUriHandler.current

    val dark         = isSystemInDarkTheme()
    val bgColor      = if (dark) Color.Black else Color.White
    val contentColor = if (dark) Color.White else Color.Black

    val onPlayClick = {
        movie?.trailerUrl?.takeIf { it.isNotBlank() }?.let {
            uriHandler.openUri(it)
        } ?: scope.launch {
            snackbarHostState.showSnackbar("No trailer available")
        }
    }

    Scaffold(
        containerColor = bgColor,
        snackbarHost   = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                modifier        = Modifier.statusBarsPadding(),
                title           = {},
                navigationIcon  = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter           = painterResource(R.drawable.ic_back),
                            contentDescription = "Back",
                            tint               = contentColor,
                            modifier           = Modifier.size(22.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            movie?.let {
                                viewModel.addToWatchlist()
                                snackbarHostState.showSnackbar("${it.title} added to watchlist")
                            }
                        }
                    }) {
                        Icon(
                            painter           = painterResource(R.drawable.ic_watchlist),
                            contentDescription = "Save to Watchlist",
                            tint               = contentColor,
                            modifier           = Modifier.size(22.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor             = bgColor,
                    navigationIconContentColor = contentColor,
                    titleContentColor          = contentColor,
                    actionIconContentColor     = contentColor
                )
            )
        },
        bottomBar = {
            AnimatedNavigationBar(
                navController     = navController,
                currentRoute      = Screen.MovieDetails.route,
                currentLanguage   = currentLanguage,
                onNavigate        = { route -> navController.navigate(route) },
                onLanguageChange  = onLanguageChange
            )
        }
    ) { innerPadding ->
        val cfg         = LocalConfiguration.current
        val isLandscape = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (isLandscape) {
            Row(
                Modifier
                    .fillMaxSize()
                    .background(bgColor)
                    .padding(innerPadding)
            ) {
                MediaSection(
                    movie       = movie,
                    onPlayClick = onPlayClick as () -> Unit ,
                    modifier    = Modifier
                        .weight(1f)
                        .height(430.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(Modifier.width(16.dp))
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(bgColor)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text     = movie?.title.orEmpty(),
                        color    = contentColor,
                        style    = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    GenreRow(movie, contentColor)
                    SynopsisSection(synopsis, contentColor)
                    ActorsSection(contentColor)
                }
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .background(bgColor)
                    .padding(innerPadding)
            ) {
                item {
                    MediaSection(
                        movie       = movie,
                        onPlayClick = onPlayClick as () -> Unit,
                        modifier    = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
                item {
                    Text(
                        text     = movie?.title.orEmpty(),
                        color    = contentColor,
                        style    = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                item { GenreRow(movie, contentColor) }
                item {
                    Spacer(Modifier.height(16.dp))
                    SynopsisSection(synopsis, contentColor)
                }
                item {
                    Spacer(Modifier.height(16.dp))
                    ActorsSection(contentColor)
                }
            }
        }
    }
}

@Composable
private fun SynopsisSection(text: String, contentColor: Color) {
    Spacer(Modifier.height(8.dp))
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(
            text     = "Synopsis",
            color    = contentColor,
            style    = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text  = text.ifBlank { "Loading synopsis…" },
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun GenreRow(movie: Movie?, contentColor: Color) {
    Spacer(Modifier.height(8.dp))
    val genres = movie?.let {
        listOfNotNull(it.genre_one, it.genre_two, it.genre_three)
            .filter { g -> g.isNotBlank() }
    } ?: emptyList()
    if (genres.isEmpty()) return
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        genres.forEach { genre ->
            AssistChip(
                onClick = { },
                label   = { Text(genre, color = contentColor) },
                colors  = AssistChipDefaults.assistChipColors(
                    containerColor = contentColor.copy(alpha = 0.12f),
                    labelColor     = contentColor
                )
            )
        }
    }
}

@Composable
private fun ActorsSection(contentColor: Color) {
    Spacer(Modifier.height(16.dp))
    Text(
        text     = "Actors",
        color    = contentColor,
        style    = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding       = PaddingValues(horizontal = 16.dp)
    ) {
        items(5) { idx ->
            ActorCard(idx)
        }
    }
}

@Composable
private fun ActorCard(index: Int) {
    Card(
        modifier  = Modifier.size(width = 100.dp, height = 160.dp),
        shape     = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Image(
            painter           = painterResource(R.drawable.bignight),
            contentDescription = "Actor ${index + 1}",
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun MediaSection(
    movie: Movie?,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        val ctx   = LocalContext.current
        val resId = remember(movie?.imageName) {
            movie?.let {
                ctx.resources.getIdentifier(it.imageName, "drawable", ctx.packageName)
            } ?: R.drawable.bignight
        }
        AsyncImage(
            model               = resId,
            contentDescription  = movie?.title.orEmpty(),
            contentScale        = ContentScale.Crop,
            modifier            = Modifier.fillMaxSize()
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.7f)
                    )
                )
        )
        IconButton(
            onClick  = onPlayClick,
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.6f))
        ) {
            Icon(
                painter           = painterResource(R.drawable.ic_play),
                contentDescription = "Play",
                tint               = Color.White,
                modifier           = Modifier.size(32.dp)
            )
        }
    }
}
