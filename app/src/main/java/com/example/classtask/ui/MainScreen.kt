package com.example.classtask.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.classtask.R
import com.example.classtask.data.UnsplashItem

private enum class TopBarTabs(@StringRes val resId: Int) {
    IMAGES(R.string.tab_images),
    COLLECTIONS(R.string.tab_collections)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    unsplashViewModel: UnsplashViewModel,
    openDetails: (UnsplashItem) -> Unit
) {
    val selected = remember { mutableIntStateOf(0) }

    Column {
        SecondaryTabRow(selectedTabIndex = selected.intValue) {
            TopBarTabs.entries.forEachIndexed { index, tab ->
                Tab(
                    modifier = Modifier.height(48.dp),
                    selected = selected.intValue == index,
                    onClick = { selected.intValue = index }
                ) {
                    Text(text = stringResource(tab.resId))
                }
            }
        }

        if (TopBarTabs.entries[selected.intValue] == TopBarTabs.IMAGES) {
            ImagesScreen(
                unsplashViewModel = unsplashViewModel,
                openDetails = openDetails
            )
        } else {
            CollectionsScreen()
        }
    }
}
