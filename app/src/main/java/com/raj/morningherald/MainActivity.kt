package com.raj.morningherald

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.raj.morningherald.presentation.navigation.NewsNavigation
import com.raj.morningherald.presentation.theme.MorningHeraldTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MorningHerald : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MorningHeraldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewsNavigation()
                }
            }
        }
    }
}
