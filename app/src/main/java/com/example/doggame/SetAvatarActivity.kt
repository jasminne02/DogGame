package com.example.doggame

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doggame.datamanager.AppDatabase
import com.example.doggame.datamanager.LoggedUser
import com.example.doggame.service.AvatarService
import com.example.doggame.ui.theme.DogGameTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.ByteArrayOutputStream

class SetAvatarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SetAvatarScreen()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SetAvatarPreview() {
    SetAvatarScreen()
}

@Composable
fun SetAvatarScreen() {
    DogGameTheme (dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GetAvatarForm(modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
        }
    }
}

@Composable
fun GetAvatarForm(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val radioOptions = listOf("Robot", "Monster", "Robot Head", "Kitten")

    var selectedOption by remember { mutableStateOf("Robot") }
    var avatarImage by remember { mutableStateOf<Bitmap?>(null) }
    var imageResource by remember { mutableStateOf<Painter?>(null) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(R.string.set_avatar),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Text(text = stringResource(R.string.select_option))

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            radioOptions.forEach { text ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = {selectedOption = text}
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }

        Image(painter = imageResource ?: painterResource(R.drawable.default_avatar),
            contentDescription = stringResource(R.string.avatar_image))

        Row (
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        avatarImage = AvatarService.getAvatar(selectedOption)
                        avatarImage?.let {
                            imageResource = BitmapPainter(it.asImageBitmap())
                        }
                    }
                }
            ) {
                Text(text = stringResource(R.string.get_avatar), fontSize = 20.sp)
            }

            Button(
                onClick = {
                    avatarImage?.let { changeUserAvatar(it, context) }
                    context.startActivity(Intent(context, ProfileActivity::class.java))
                }
            ) {
                Text(text = stringResource(R.string.set_avatar), fontSize = 20.sp)
            }
        }
    }
}

fun changeUserAvatar(avatarImage: Bitmap, context: Context) {
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDAO()

    val outputStream = ByteArrayOutputStream()
    avatarImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    val image = outputStream.toByteArray()

    try {
        LoggedUser.getUsername()?.let { userDao.updateUserAvatar(it, image) }
    } catch (e: Exception) {
        Log.e("DatabaseError", "Error reading from database", e)
    }

    LoggedUser.setAvatarImage(avatarImage)
}
