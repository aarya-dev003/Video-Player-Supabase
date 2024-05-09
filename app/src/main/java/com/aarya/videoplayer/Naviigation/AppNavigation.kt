package com.aarya.videoplayer.Naviigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aarya.videoplayer.data.remote.VideoRepo
import com.aarya.videoplayer.views.FullPage
import com.aarya.videoplayer.views.HomeScreen
import com.aarya.videoplayer.views.SearchPage
import com.aarya.videoplayer.views.UploadScreen

@Composable
fun AppNavigation(remoteRepo: VideoRepo) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.route) {
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(remoteRepo, navController)
        }
        composable(route = AppScreens.UploadScreen.route) {
            UploadScreen(navController)
        }
        composable(
            route = "${AppScreens.FullPage.route}/{videoId}",
            arguments = listOf(navArgument("videoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId")
            if (videoId != null) {
                FullPage(remoteRepo, navController, videoId)
            } else {
                // Handle missing or invalid videoId
            }
        }

        composable(
            route = "${AppScreens.SearchPage.route}/{videoTitle}",
            arguments = listOf(navArgument("videoTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val videoTitle = backStackEntry.arguments?.getString("videoTitle")
            Log.i("VideoTitle", "$videoTitle")
            if (videoTitle != null) {
                SearchPage(remoteRepo, navController, videoTitle)
            } else {
                // Handle missing or invalid videoId
            }
        }
    }
}
