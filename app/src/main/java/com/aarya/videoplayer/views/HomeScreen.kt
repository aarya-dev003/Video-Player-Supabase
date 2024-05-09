package com.aarya.videoplayer.views


import android.media.browse.MediaBrowser
import android.net.Uri
import android.util.Log
import android.view.Display.Mode
import android.widget.ImageButton
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.aarya.videoplayer.R
import com.aarya.videoplayer.data.remote.VideoRepo
import com.aarya.videoplayer.model.VideoModel
import com.aarya.videoplayer.module.supabase
import com.aarya.videoplayer.ui.theme.subtleGrey
import com.aarya.videoplayer.ui.theme.subtleWhite
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(remoteRepo: VideoRepo,navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    var videos by remember { mutableStateOf(emptyList<VideoModel>()) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val fetchedVideos = remoteRepo.getAllVideos()
                videos = fetchedVideos
            } catch (e: Exception) {

            }
        }
    }
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 4.dp,
                bottom = 8.dp
            )
            .fillMaxSize()
    ) {
        Row {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Video Title") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            Log.d("searchQuery", searchQuery)
                            navController.navigate("SearchPage/$searchQuery") {
                                launchSingleTop = true
                                popUpTo("SearchPage") { inclusive = true }
                            }
                        },
                    ) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            )
        }



        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn (
            modifier = Modifier.padding(
                bottom = 8.dp
            )
        ) {
            items(videos.size) { index ->
                val video = videos[index]
                Box(modifier = Modifier
                    .padding(top = 6.dp,
                        bottom = 6.dp)
                    .clip(RoundedCornerShape(12.dp))) {
                    VideoItem(
                        video = video,
                        navController = navController,
                        onVideoClick = { selectedVideo ->
                            navController.navigate("FullPage/${video.id}") {
                                launchSingleTop = true
                                popUpTo("FullPage") { inclusive = true }
                            }
                            // Pass the selectedVideo object to the FullPage composable
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    Log.d("videoUrl", videoUrl)
    val context = LocalContext.current
    val exoPlayer = remember {
//        ExoPlayer.Builder(context).build().apply {
//            val mediaItem = MediaBrowser.MediaItem.fromUri(videoUrl)
//            setMediaItem(mediaItem)
//            prepare()
//        }
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val mediaItem = MediaItem.fromUri(videoUrl)
                setMediaItem(mediaItem)
                prepare()
                //play()
            }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
            }
        },
        modifier = modifier
    )
}

@Composable
fun VideoItem(video: VideoModel, navController: NavHostController, onVideoClick: (VideoModel) -> Unit) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .background(subtleWhite)
        .padding(top = 8.dp)
        .clickable {
        onVideoClick(video)
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Log.d("videoUrl from video item", video.url)
            VideoPlayer(
                videoUrl = video.url,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = video.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 19.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = video.channelName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    textAlign = TextAlign.Left,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = formatTimestamp(video.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Right,
                    fontSize = 12.sp
                )
            }

        }
    }
}
@Composable
fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
    return format.format(date)
}