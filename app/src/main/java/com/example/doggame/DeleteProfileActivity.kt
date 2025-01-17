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
import com.example.doggame.ui.theme.DogGameTheme

class DeleteProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeleteProfileScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteProfilePreview() {
    DeleteProfileScreen()
}

@Composable
fun DeleteProfileScreen() {
    DogGameTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ConfirmationForm(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
        }
    }
}

@Composable
fun ConfirmationForm(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val invalidPasswordMessage: String = stringResource(R.string.wrong_password_message)

    var password by rememberSaveable { mutableStateOf("") }
    var textMessage by remember { mutableStateOf("") }

    val userPassword = LoggedUser.getPassword()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(R.string.delete_profile),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Text(text = stringResource(R.string.delete_profile_message))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Confirm Your Password") },
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
                if (userPassword != password) {
                    textMessage = invalidPasswordMessage
                } else {
                    deleteUser(context)
                    context.startActivity(Intent(context, MainActivity::class.java))
                }
            },
        ) {
            Text(text = stringResource(R.string.delete_profile), fontSize = 20.sp)
        }
    }
}

fun deleteUser(context: Context) {
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDAO()

    try {
        LoggedUser.getUsername()?.let { userDao.deleteUser(it) }
    } catch (e: Exception) {
        Log.e("DatabaseError", "Error deleting from database", e)
    }

    LoggedUser.logout()
}
