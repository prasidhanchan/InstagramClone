package com.instagramclone.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.instagramclone.ui.components.IGButton
import com.instagramclone.util.constants.Utils

@Composable
fun HomeScreen(
    logOut: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Utils.IgBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text(
//                text = if (SupabaseAuthRepositoryImpl.currentUser != null) "Logged In" else "Not Logged In",
//                style = TextStyle(
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Normal,
//                    color = Color.White
//                )
//            )
            Spacer(modifier = Modifier.height(20.dp))
            IGButton(
                text = "Log out",
                isLoading = false,
                onClick = logOut
            )
        }
    }
}