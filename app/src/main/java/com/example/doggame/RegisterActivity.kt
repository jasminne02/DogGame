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
import com.example.doggame.datamanager.Validation
import com.example.doggame.ui.theme.DogGameTheme


class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegisterScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}


@Composable
fun RegisterScreen() {
    DogGameTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RegisterFieldsInput(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
        }
    }
}


@Composable
fun RegisterFieldsInput(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val requiredFieldsMessage: String = stringResource(R.string.required_fields_message)
    val invalidUsernameMessage: String = stringResource(R.string.invalid_username_message)
    val invalidPasswordMessage: String = stringResource(R.string.invalid_password_message)
    val unequalPasswordsMessage: String = stringResource(R.string.unequal_passwords_message)

    var textMessage by remember { mutableStateOf(requiredFieldsMessage) }

    var username by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatedPassword by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(text = stringResource(R.string.register),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        OutlinedTextField(
            value = username.trim(),
            onValueChange = { username = it.lowercase() },
            label = { Text("Username*") }
        )

        OutlinedTextField(
            value = password.trim(),
            onValueChange = { password = it },
            label = { Text("Enter Password*") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        OutlinedTextField(
            value = repeatedPassword.trim(),
            onValueChange = { repeatedPassword = it },
            label = { Text("Repeat Password*") },
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
                if (Validation().checkRequiredFields(username, password, repeatedPassword)) {
                    textMessage = requiredFieldsMessage
                } else if (!Validation().isUsernameUnique(username, context)) {
                    textMessage = invalidUsernameMessage
                } else if (Validation().arePasswordsDifferent(password, repeatedPassword)){
                    textMessage = unequalPasswordsMessage
                } else if (!Validation().isPasswordValid(password)) {
                    textMessage = invalidPasswordMessage
                } else {
                    createUser(username, password, context)
                    context.startActivity(Intent(context, MenuActivity::class.java))
                }
            },
        ) {
            Text(text = stringResource(R.string.register), fontSize = 20.sp)
        }
    }
}

fun createUser(username: String, password: String, context: Context){
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDAO()
    val user = User(username = username, password = password,
        bestScore = null, avatarImage = null)

    try {
        userDao.insert(user)
    } catch (e: Exception) {
        Log.e("DatabaseError", "Error writing to database", e)
    }

    LoggedUser.login(user)
}
