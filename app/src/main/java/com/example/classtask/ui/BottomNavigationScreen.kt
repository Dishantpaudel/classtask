package com.example.classtask.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationScreen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : BottomNavigationScreen("home", "Home", Icons.Default.Home)
    data object About : BottomNavigationScreen("about", "About", Icons.Default.Info)
}
