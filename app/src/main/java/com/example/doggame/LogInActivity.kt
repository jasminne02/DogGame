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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doggame.datamanager.AppDatabase
import com.example.doggame.datamanager.LoggedUser
import com.example.doggame.datamanager.User
import com.example.doggame.ui.theme.DogGameTheme

class LogInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}


@Composable
fun LoginScreen() {
    DogGameTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginFieldsInput(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
        }
    }
}

@Composable
fun LoginFieldsInput(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val invalidUsernameMessage: String = stringResource(R.string.not_existing_user_message)
    val invalidPasswordMessage: String = stringResource(R.string.wrong_password_message)

    var textMessage by remember { mutableStateOf("") }

    var username by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(text = stringResource(R.string.log_in),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        OutlinedTextField(
            value = username.trim(),
            onValueChange = { username = it.lowercase() },
            label = { Text("Username") }
        )

        OutlinedTextField(
            value = password.trim(),
            onValueChange = { password = it },
            label = { Text("Enter Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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
                val user: User? = searchUserByUsername(username, context)
                if (user == null) {
                    textMessage = invalidUsernameMessage
                } else if (password != user.password) {
                    textMessage = invalidPasswordMessage
                } else {
                    LoggedUser.login(user)
                    context.startActivity(Intent(context, MenuActivity::class.java))
                }
            },
        ) {
            Text(text = stringResource(R.string.log_in), fontSize = 20.sp)
        }
    }
}

fun searchUserByUsername(username: String, context: Context) : User? {
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDAO()
    var user: User? = null

    try {
        user = userDao.findByUsername(username)
    } catch (e: Exception) {
        Log.e("DatabaseError", "Error reading from database", e)
    }

    return user
}
