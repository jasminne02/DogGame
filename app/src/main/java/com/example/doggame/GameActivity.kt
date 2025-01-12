package com.example.doggame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.doggame.ui.theme.DogGameTheme

class StartGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameActivityScreen()
        }
    }
}


@Composable
fun GameActivityScreen() {
    DogGameTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Game(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
        }
    }
}

@Composable
fun Game(modifier: Modifier = Modifier) {
    val context = LocalContext.current

}
