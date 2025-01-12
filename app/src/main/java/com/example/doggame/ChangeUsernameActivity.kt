package com.example.doggame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doggame.datamanager.AppDatabase
import com.example.doggame.datamanager.LoggedUser
import com.example.doggame.datamanager.Validation
import com.example.doggame.ui.theme.DogGameTheme

class ChangeUsernameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChangeUsernameScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangeUsernamePreview() {
    ChangeUsernameScreen()
}

@Composable
fun ChangeUsernameScreen() {
    DogGameTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ChangeUsernameForm(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
        }
    }
}

@Composable
fun ChangeUsernameForm(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val invalidUsernameMessage: String = stringResource(R.string.invalid_username_message)

    var username by rememberSaveable { mutableStateOf("") }
    var textMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(R.string.change_username),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it.lowercase() },
            label = { Text("Enter new username") }
        )

        Text(text = textMessage,
            style = TextStyle(
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.tertiary
            )
        )

        Button(
            onClick = {
                if (!Validation().isUsernameUnique(username, context)) {
                    textMessage = invalidUsernameMessage
                } else {
                    changeUsername(username, context)
                    context.startActivity(Intent(context, ProfileActivity::class.java))
                }
            },
        ) {
            Text(text = stringResource(R.string.change), fontSize = 20.sp)
        }
    }
}

fun changeUsername(username: String, context: Context) {
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDAO()

    try {
        LoggedUser.getUsername()?.let { userDao.updateUserUsername(it, username) }
    } catch (e: Exception) {
        Log.e("DatabaseError", "Error reading from database", e)
    }

    LoggedUser.setUsername(username)
}
