package com.example.doggame

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doggame.ui.theme.DogGameTheme

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainMenuScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    MainMenuScreen()
}


@Composable
fun MainMenuScreen() {
    DogGameTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MenuButtons(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
        }
    }
}

@Composable
fun MenuButtons(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(R.string.main_menu),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Button(
            onClick = {
                context.startActivity(Intent(context, StartGameActivity::class.java))
            },
        ) {
            Text(text = stringResource(R.string.start_game), fontSize = 20.sp)
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, DashboardActivity::class.java))
            },
        ) {
            Text(text = stringResource(R.string.dashboard), fontSize = 20.sp)
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, ProfileActivity::class.java))
            },
        ) {
            Text(text = stringResource(R.string.profile), fontSize = 20.sp)
        }
    }
}
