package com.aarya.videoplayer.data.remote

import com.aarya.videoplayer.model.VideoModel

interface  VideoRepo{
    suspend fun getAllVideos(): List<VideoModel>
    suspend fun getVideoByTitle(title: String): VideoModel?
    suspend fun storeVideos(video: VideoModel): Boolean
    suspend fun getVideoById(videoId: Long):VideoModel?

}