package com.example.doggame

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.doggame.ui.theme.DogGameTheme
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}


@Composable
fun MainScreen() {
    DogGameTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TitleWithImageAndButtons(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
        }
    }
}


@Composable
fun TitleWithImageAndButtons(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageResource = painterResource(R.drawable.dog_icon)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(R.string.app_name),
            style = TextStyle(
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Image(painter = imageResource, contentDescription = stringResource(R.string.app_name))

        Button(
            onClick = {context.startActivity(Intent(context, LogInActivity::class.java))},
        ) {
            Text(text = stringResource(R.string.log_in), fontSize = 20.sp)
        }

        Button(
            onClick = {context.startActivity(Intent(context, RegisterActivity::class.java))},
        ) {
            Text(text = stringResource(R.string.register), fontSize = 20.sp)
        }
    }

}
