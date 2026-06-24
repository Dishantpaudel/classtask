package com.example.classtask.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.classtask.R
import com.example.classtask.data.UnsplashItem

@Composable
fun ImagesScreen(
    unsplashViewModel: UnsplashViewModel,
    openDetails: (UnsplashItem) -> Unit
) {
    val images by unsplashViewModel.images.collectAsStateWithLifecycle()
    var search by rememberSaveable { mutableStateOf("") }
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        state = pullToRefreshState,
        isRefreshing = images is UiState.Refreshing,
        onRefresh = { unsplashViewModel.fetchImage() }
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { letter -> search = letter },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions {
                    unsplashViewModel.search(search)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (images) {
                is UiState.Error -> Text("Unable to fetch images")
                is UiState.Success -> {
                    ImagesContent(
                        images = (images as UiState.Success<List<UnsplashItem>>).data,
                        openDetails = openDetails
                    )
                }
                is UiState.Loading -> ImagesLoading()
                is UiState.Refreshing -> { }
            }
        }
    }
}

@Composable
private fun ImagesContent(
    images: List<UnsplashItem>,
    openDetails: (UnsplashItem) -> Unit
) {
    LazyColumn {
        items(images) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .clickable { openDetails(item) }
            ) {
                Box {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.urls?.regular)
                                .build()
                        ),
                        contentDescription = stringResource(R.string.description_image),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.95f)
                                        )
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Text(
                                text = item.user?.name ?: "-",
                                fontWeight = FontWeight.Bold
                            )
                            item.description?.let { description ->
                                Text(
                                    text = description,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ImagesLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}
