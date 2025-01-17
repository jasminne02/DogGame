package com.example.doggame

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doggame.datamanager.AppDatabase
import com.example.doggame.datamanager.User
import com.example.doggame.ui.theme.DogGameTheme

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardScreen()
}

@Composable
fun DashboardScreen() {
    DogGameTheme(dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val context = LocalContext.current

             Column(
                modifier = Modifier.fillMaxSize()
             ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSecondaryContainer)
                        .padding(20.dp).offset(x = (-20).dp)
                ) {
                    IconButton(
                        onClick = {context.startActivity(Intent(context, MenuActivity::class.java))},
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Button",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Dashboard(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
fun Dashboard(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val avatarResource = painterResource(R.drawable.default_avatar)
    val users = getAllUsers(context)?.sortedByDescending { it.bestScore }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = stringResource(R.string.dashboard),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        if (users != null) {
            LazyColumn {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "No", fontSize = 16.sp, modifier = Modifier.weight(1f))
                        Text(text = "Avatar", fontSize = 16.sp, modifier = Modifier.weight(2f))
                        Text(text = "Username", fontSize = 16.sp, modifier = Modifier.weight(2f))
                        Text(text = "Score", fontSize = 16.sp, modifier = Modifier.weight(1f))
                    }
                }

                itemsIndexed(users) { index, user ->

                    val bestScore = user.bestScore ?: 0

                    var imageResource = avatarResource
                    if (user.avatarImage != null) {
                        imageResource = BitmapFactory
                            .decodeByteArray(user.avatarImage, 0, user.avatarImage.size)
                            .asImageBitmap().let{ BitmapPainter(it) }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "${index + 1}",
                            modifier = Modifier.weight(1f).padding(16.dp).offset(x = 10.dp),
                            style = MaterialTheme.typography.bodySmall
                        )

                        Image(
                            painter = imageResource,
                            contentDescription = stringResource(R.string.avatar_image),
                            modifier = Modifier.size(40.dp).weight(2f)
                                .padding(8.dp).offset(x = (-25).dp)
                        )

                        Text(
                            text = user.username,
                            modifier = Modifier.weight(2f).padding(8.dp).offset(x = 20.dp),
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = bestScore.toString(),
                            modifier = Modifier.weight(1f).padding(8.dp).offset(x = 15.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

fun getAllUsers(context: Context): List<User>? {
    val db = AppDatabase.getInstance(context)
    val userDao = db.userDAO()
    var users: List<User>? = null

    try {
       users = userDao.getAll()
    } catch (e: Exception) {
        Log.e("DatabaseError", "Error reading from database", e)
    }

    return users
}
