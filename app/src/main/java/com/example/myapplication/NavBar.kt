package com.example.myapplication

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
    // Define your tabs
    val navigationItems = listOf(
        NavigationItem(Screen.Home.route,      R.drawable.ic_home,            24.dp),
        NavigationItem(Screen.Search.route,    R.drawable.ic_search,          24.dp),
        NavigationItem(Screen.Watchlist.route, R.drawable.ic_watchlist,       24.dp),
        NavigationItem(Screen.Language.route,  R.drawable.ic_change_language, 24.dp)
    )

    val barColor = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFFFFFFF)

    NavigationBar(
        modifier      = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp)),
        containerColor = barColor,
        tonalElevation = 8.dp
    ) {
        navigationItems.forEach { item ->
            val isSelected = currentRoute == item.route

            // ② animate icon color
            val iconTint by animateColorAsState(
                targetValue   = if (isSelected) Color.Black else Color.LightGray,
                animationSpec = tween(durationMillis = 300)
            )

            NavigationBarItem(
                modifier = Modifier.weight(1f),
                selected      = isSelected,
                onClick       = { if (!isSelected) onNavigate(item.route) },
                colors        = NavigationBarItemDefaults.colors(
                    selectedIconColor   = Color.Red,
                    unselectedIconColor = Color.LightGray,
                    indicatorColor      = Color.Transparent
                ),
                icon          = {
                    Icon(
                        painter           = painterResource(id = item.icon),
                        contentDescription = item.route,
                        tint               = iconTint,
                        modifier           = Modifier.size(item.iconSize)
                    )
                }
            )
        }
    }
}

data class NavigationItem(
    val route: String,
    val icon: Int,
    val iconSize: Dp
)
