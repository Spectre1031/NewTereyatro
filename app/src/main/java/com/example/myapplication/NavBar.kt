package com.example.myapplication

import android.content.res.Configuration
import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AnimatedNavigationBar(
    navController: NavController,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val navigationItems = listOf(
        NavigationItem(Screen.Home.route, "Home", R.drawable.ic_home, 30.dp),
        NavigationItem(Screen.Search.route, "Search", R.drawable.ic_search, 24.dp),
        NavigationItem(Screen.Watchlist.route, "Watchlist", R.drawable.ic_watchlist, 24.dp),
        NavigationItem(Screen.Language.route, "Language", R.drawable.ic_change_language, 35.dp)
    )

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    val darkRed = Color(0xFF8B0000)

    if (isPortrait) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
        ) {
            NavigationBar(
                containerColor = darkRed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                navigationItems.forEach { item ->
                    val isSelected = currentRoute == item.route
                    val iconColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else Color.LightGray,
                        animationSpec = tween(300)
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.description,
                                modifier = Modifier.size(item.iconSize),
                                tint = iconColor
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = darkRed
                        )
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(80.dp)
                .background(darkRed),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            navigationItems.forEach { item ->
                val isSelected = currentRoute == item.route
                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else Color.LightGray,
                    animationSpec = tween(300)
                )

                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .clickable {
                            if (!isSelected) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.description,
                        modifier = Modifier.size(item.iconSize),
                        tint = iconColor
                    )
                }
            }
        }
    }
}

data class NavigationItem(val route: String, val description: String, val icon: Int, val iconSize: Dp)