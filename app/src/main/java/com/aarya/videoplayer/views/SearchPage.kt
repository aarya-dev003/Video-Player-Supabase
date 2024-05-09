package com.aarya.videoplayer.views

import android.util.Log
import androidx.compose.foundation.background
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
fun SearchPage(
    remoteRepo: VideoRepo,
    navController: NavController,
    videoId: String
){
    Log.d("searchQuery from search page", videoId)
    Box(modifier = Modifier.background(Color.White)) {
        var video by remember { mutableStateOf<VideoModel?>(null) }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val arguments = navBackStackEntry?.arguments

        LaunchedEffect(videoId) {
            videoId.let {
                val fetchedVideo = remoteRepo.getVideoByTitle(it)
                video = fetchedVideo
                Log.d("Video Id", "$video.id")
            }
        }

        video?.let {
            Box(modifier = Modifier.clip(RoundedCornerShape(12.dp))) {
                FullPagelayout(it, navController)
            }
        }
    }
}
