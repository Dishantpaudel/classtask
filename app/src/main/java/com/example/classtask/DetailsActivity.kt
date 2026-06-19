package com.example.classtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.classtask.data.model.PhotoDetail
import com.example.classtask.data.model.UnsplashItem
import com.example.classtask.ui.DetailsViewModel
import com.example.classtask.ui.theme.ClassTaskTheme

const val EXTRA_UNSPLASH_IMAGE = "extra_unsplash_image"

class DetailsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        @Suppress("DEPRECATION")
        val item = intent.getParcelableExtra<UnsplashItem>(EXTRA_UNSPLASH_IMAGE)

        if (item == null) {
            setContent {
                ClassTaskTheme {
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text("No image data")
                        }
                    }
                }
            }
            return
        }

        val viewModel: DetailsViewModel by viewModels { DetailsViewModel.factory(item.id) }

        setContent {
            ClassTaskTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val detail by viewModel.detail.collectAsStateWithLifecycle()
                    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
                    var liked by remember { mutableStateOf(false) }

                    if (isLoading) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator()
                        }
                    } else {
                        DetailsContent(
                            item = item,
                            detail = detail,
                            liked = liked,
                            onLikeToggle = { liked = !liked },
                            onBack = { finish() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailsContent(
    item: UnsplashItem,
    detail: PhotoDetail?,
    liked: Boolean,
    onLikeToggle: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val locationText = detail?.location?.let { loc ->
        when {
            loc.name != null -> loc.name
            loc.city != null && loc.country != null -> "${loc.city}, ${loc.country}"
            loc.city != null -> loc.city
            loc.country != null -> loc.country
            else -> null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero image with back button and location overlay
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(detail?.urls?.regular ?: item.urls.regular)
                        .build()
                ),
                contentDescription = detail?.description ?: item.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            if (locationText != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.55f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = locationText,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        // User row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(detail?.user?.profileImage?.medium ?: item.user.profileImage?.medium)
                        .build()
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = detail?.user?.name ?: item.user.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Download")
            }
            IconButton(onClick = onLikeToggle) {
                Icon(
                    imageVector = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like"
                )
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
        }

        Divider(modifier = Modifier.padding(horizontal = 12.dp))

        // EXIF grid
        ExifGrid(detail = detail)

        Divider(modifier = Modifier.padding(horizontal = 12.dp))

        // Stats row: views / likes / downloads
        StatsRow(detail = detail, item = item)

        Divider(modifier = Modifier.padding(horizontal = 12.dp))

        // Tags
        val tags = detail?.tags?.map { it.title } ?: emptyList()
        if (tags.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    SuggestionChip(
                        onClick = { },
                        label = { Text(text = tag, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ExifGrid(detail: PhotoDetail?) {
    val exif = detail?.exif
    val cameraModel = listOfNotNull(exif?.make, exif?.model)
        .joinToString(" ").ifBlank { "-" }
    val dimension = if (detail?.width != null && detail.height != null) {
        "${detail.width} × ${detail.height}"
    } else "-"

    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ExifItem(label = "Camera", value = cameraModel, modifier = Modifier.weight(1f))
            ExifItem(
                label = "Aperture",
                value = exif?.aperture?.let { "f/$it" } ?: "-",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            ExifItem(
                label = "Focal Length",
                value = exif?.focalLength?.let { "${it}mm" } ?: "-",
                modifier = Modifier.weight(1f)
            )
            ExifItem(
                label = "Shutter Speed",
                value = exif?.exposureTime ?: "-",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            ExifItem(
                label = "ISO",
                value = exif?.iso?.toString() ?: "-",
                modifier = Modifier.weight(1f)
            )
            ExifItem(label = "Dimensions", value = dimension, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ExifItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StatsRow(detail: PhotoDetail?, item: UnsplashItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(label = "Views", value = formatCount(detail?.views))
        StatItem(label = "Likes", value = formatCount(detail?.likes ?: item.likes))
        StatItem(label = "Downloads", value = formatCount(detail?.downloads))
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}

private fun formatCount(count: Int?): String {
    if (count == null) return "-"
    return when {
        count >= 1_000_000 -> "%.1fM".format(count / 1_000_000.0)
        count >= 1_000 -> "%.1fK".format(count / 1_000.0)
        else -> count.toString()
    }
}
