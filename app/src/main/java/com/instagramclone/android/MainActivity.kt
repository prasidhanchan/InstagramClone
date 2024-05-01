package com.instagramclone.android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.instagramclone.android.navigation.MainNavigation
import com.instagramclone.ui.theme.InstagramTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.R)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstagramTheme {
                MainNavigation()
            }
        }
    }
}