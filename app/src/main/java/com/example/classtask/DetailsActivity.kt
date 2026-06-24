package com.example.classtask

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.IntentCompat
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.classtask.data.UnsplashItem
import com.example.classtask.ui.theme.UnsplashTheme
import com.example.classtask.utils.EXTRA_IMAGE

class DetailsActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val image = IntentCompat.getParcelableExtra(intent, EXTRA_IMAGE, UnsplashItem::class.java)

        setContent {
            UnsplashTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.description_image)) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(R.string.description_back)
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(image?.urls?.regular)
                                    .build()
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clickable {
                                    val intent = Intent(this@DetailsActivity, ImageActivity::class.java)
                                    intent.putExtra(EXTRA_IMAGE, image)
                                    startActivity(intent)
                                },
                            contentScale = ContentScale.FillWidth,
                            contentDescription = stringResource(R.string.description_image)
                        )

                        val modifier = Modifier
                            .weight(1.0f)
                            .padding(16.dp)

                        Line(
                            modifier = modifier,
                            cell1ResId = R.string.image_camera,
                            cell1Value = image?.user?.name ?: "-",
                            cell2ResId = R.string.image_aperture,
                            cell2Value = "-"
                        )

                        Line(
                            modifier = modifier,
                            cell1ResId = R.string.image_focal_length,
                            cell1Value = "-",
                            cell2ResId = R.string.image_shutter_speed,
                            cell2Value = "-"
                        )

                        Line(
                            modifier = modifier,
                            cell1ResId = R.string.image_iso,
                            cell1Value = "-",
                            cell2ResId = R.string.image_dimensions,
                            cell2Value = if (image?.width != null && image.height != null) {
                                "${image.width} x ${image.height}"
                            } else "-"
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            thickness = 2.dp,
                            color = Color.LightGray
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                        ) {
                            Row(modifier = Modifier.weight(1.0f)) {
                                Cell(
                                    resId = R.string.image_views,
                                    value = "-",
                                    modifier = Modifier,
                                    horizontalArrangement = Arrangement.Center,
                                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                                )
                            }
                            Row(modifier = Modifier.weight(1.0f)) {
                                Cell(
                                    resId = R.string.image_downloads,
                                    value = "-",
                                    modifier = Modifier,
                                    horizontalArrangement = Arrangement.Center,
                                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                                )
                            }
                            Row(modifier = Modifier.weight(1.0f)) {
                                Cell(
                                    resId = R.string.image_likes,
                                    value = "-",
                                    modifier = Modifier,
                                    horizontalArrangement = Arrangement.Center,
                                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Line(
        modifier: Modifier,
        @StringRes cell1ResId: Int,
        cell1Value: String,
        @StringRes cell2ResId: Int,
        cell2Value: String
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Cell(resId = cell1ResId, value = cell1Value, modifier = modifier)
            Cell(resId = cell2ResId, value = cell2Value, modifier = modifier)
        }
    }

    @Composable
    fun Cell(
        @StringRes resId: Int,
        value: String,
        modifier: Modifier,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
        horizontalAlignment: androidx.compose.ui.Alignment.Horizontal = androidx.compose.ui.Alignment.Start
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = horizontalArrangement
        ) {
            Column(horizontalAlignment = horizontalAlignment) {
                Text(text = stringResource(resId), fontWeight = FontWeight.Bold)
                Text(text = value)
            }
        }
    }
}
