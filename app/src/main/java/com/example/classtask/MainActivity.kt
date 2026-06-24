package com.example.classtask

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.classtask.data.UnsplashItem
import com.example.classtask.repository.AppPreferences
import com.example.classtask.ui.AboutScreen
import com.example.classtask.ui.FavouritesScreen
import com.example.classtask.ui.MainScreen
import com.example.classtask.ui.UnsplashViewModel
import com.example.classtask.ui.theme.UnsplashTheme
import com.example.classtask.utils.EXTRA_IMAGE

class MainActivity : ComponentActivity() {

    private val unsplashViewModel: UnsplashViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val openDetails = { image: UnsplashItem ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(EXTRA_IMAGE, image)
            startActivity(intent)
        }

        val bottomBar = listOf(
            BottomNavigationScreen.Home,
            BottomNavigationScreen.Favourites,
            BottomNavigationScreen.About
        )

        val preferences = AppPreferences(this)

        setContent {
            val preferencesTheme = preferences.isDarkTheme()
            val isDarkTheme = if (preferencesTheme == -1) isSystemInDarkTheme() else preferencesTheme == 1
            val darkTheme = rememberSaveable { mutableStateOf(isDarkTheme) }
            val backstack = remember { mutableStateListOf<Any>(BottomNavigationScreen.Home) }

            UnsplashTheme(darkTheme = darkTheme.value) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = stringResource(R.string.app_name)) }
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            bottomBar.forEach { screen ->
                                NavigationBarItem(
                                    selected = backstack.lastOrNull() == screen,
                                    onClick = {
                                        if (backstack.lastOrNull() != screen) {
                                            backstack.add(screen)
                                        }
                                    },
                                    label = { Text(stringResource(screen.resId)) },
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = stringResource(screen.resId)
                                        )
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        NavDisplay(
                            backStack = backstack,
                            onBack = { backstack.removeLastOrNull() },
                            entryProvider = entryProvider {
                                entry<BottomNavigationScreen.Home> {
                                    MainScreen(
                                        unsplashViewModel = unsplashViewModel,
                                        openDetails = openDetails
                                    )
                                }
                                entry<BottomNavigationScreen.Favourites> {
                                    FavouritesScreen()
                                }
                                entry<BottomNavigationScreen.About> {
                                    AboutScreen(
                                        darkTheme = darkTheme,
                                        saveAction = { value -> preferences.setDarkTheme(value) }
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
