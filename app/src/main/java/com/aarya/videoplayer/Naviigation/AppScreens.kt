package com.aarya.videoplayer.Naviigation


sealed class AppScreens(val route: String) {
    object HomeScreen : AppScreens("HomeScreen")
    object UploadScreen : AppScreens("UploadScreen")
    object FullPage:AppScreens("FullPage")
    object SearchPage:AppScreens("SearchPage")
}