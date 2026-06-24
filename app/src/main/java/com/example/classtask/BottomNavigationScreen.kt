package com.example.classtask

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationScreen(
    @StringRes val resId: Int,
    val icon: ImageVector
) {
    data object Home : BottomNavigationScreen(
        resId = R.string.nav_home,
        icon = Icons.Default.Home
    )

    data object Favourites : BottomNavigationScreen(
        resId = R.string.nav_favourites,
        icon = Icons.Default.Favorite
    )

    data object About : BottomNavigationScreen(
        resId = R.string.nav_about,
        icon = Icons.Default.Info
    )
}
