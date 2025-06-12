package com.example.myapplication

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBarItemDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AnimatedNavigationBar(
    navController: NavHostController,
    currentRoute: String,
    currentLanguage: String,
    onNavigate: (String) -> Unit,
    onLanguageChange: (String) -> Unit
) {
    // Dialog state
    var showLanguageDialog by remember { mutableStateOf(false) }

    // The three real "destinations"
    val navigationItems = listOf(
        NavigationItem(Screen.Home.route,      R.drawable.ic_home,      24.dp),
        NavigationItem(Screen.Search.route,    R.drawable.ic_search,    24.dp),
        NavigationItem(Screen.Watchlist.route, R.drawable.ic_watchlist, 24.dp)
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
        // Regular nav items
        navigationItems.forEach { item ->
            val isSelected = currentRoute == item.route
            val iconTint by animateColorAsState(
                targetValue   = if (isSelected) Color.DarkGray else Color.LightGray,
                animationSpec = tween(300)
            )

            NavItem(
                route    = item.route,
                iconRes  = item.icon,
                iconSize = item.iconSize,
                selected = isSelected,
                tint     = iconTint
            ) {
                if (!isSelected) onNavigate(item.route)
            }
        }

        // Language-toggle button
        val langTint by animateColorAsState(
            targetValue   = Color.LightGray,
            animationSpec = tween(300)
        )

        NavigationBarItem(
            selected    = false,
            onClick     = { showLanguageDialog = true },
            colors      = colors(
                selectedIconColor   = Color.Transparent,
                unselectedIconColor = langTint,
                indicatorColor      = Color.Transparent
            ),
            icon = {
                Icon(
                    painter           = painterResource(R.drawable.ic_change_language),
                    contentDescription = "Language",
                    tint               = langTint,
                    modifier           = Modifier.size(24.dp)
                )
            }
        )
    }

    // Popup to confirm language switch
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title   = { Text("Switch Language") },
            text    = {
                Text(
                    if (currentLanguage == "en")
                        "Switch interface to Tagalog?"
                    else
                        "Switch interface to English?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val newLang = if (currentLanguage == "en") "tl" else "en"
                    onLanguageChange(newLang)
                    showLanguageDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
private fun RowScope.NavItem(
    route: String,
    iconRes: Int,
    iconSize: Dp,
    selected: Boolean,
    tint: Color,
    onClick: () -> Unit
) {
    NavigationBarItem(
        modifier  = Modifier.weight(1f),
        selected  = selected,
        onClick   = onClick,
        colors    = colors(
            selectedIconColor   = Color.Red,
            unselectedIconColor = Color.LightGray,
            indicatorColor      = Color.Transparent
        ),
        icon = {
            Icon(
                painter           = painterResource(id = iconRes),
                contentDescription = route,
                tint               = tint,
                modifier           = Modifier.size(iconSize)
            )
        }
    )
}

data class NavigationItem(
    val route: String,
    val icon: Int,
    val iconSize: Dp
)
