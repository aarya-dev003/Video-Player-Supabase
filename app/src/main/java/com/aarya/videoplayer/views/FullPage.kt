package com.aarya.videoplayer.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.aarya.videoplayer.data.remote.VideoRepo
import com.aarya.videoplayer.model.VideoModel
@Composable
fun FullPage(
    remoteRepo: VideoRepo,
    navController: NavController,
    videoId: String
) {
    Box(modifier = Modifier.background(Color.White)) {
        var video by remember { mutableStateOf<VideoModel?>(null) }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val arguments = navBackStackEntry?.arguments
//        val videoId = arguments?.getString("videoId")
        LaunchedEffect(videoId) {
            videoId?.let {
                val fetchedVideo = remoteRepo.getVideoById(it.toLong())
                video = fetchedVideo
            }
        }

        video?.let {
            Box(modifier = Modifier.clip(RoundedCornerShape(12.dp))) {
                FullPagelayout(it, navController)
            }
        }
    }
}

@Composable
fun FullPagelayout(video: VideoModel, navController: NavController) {
    Box (
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    16.dp,
                    top = 48.dp
                )
                .align(Alignment.Center)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Log.d("videoUrl from video item", video.url)

            Text(
                text = video.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.padding(16.dp))
            VideoPlayer(
                videoUrl = video.url,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = video.channelName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = formatTimestamp(video.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = video.description,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.W400,
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}