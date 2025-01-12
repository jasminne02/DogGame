package com.example.doggame

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.doggame.datamanager.LoggedUser
import com.example.doggame.ui.theme.DogGameTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileScreen()
}

@Composable
fun ProfileScreen() {
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

            ButtonsAndAvatarImage(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
fun ButtonsAndAvatarImage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var imageResource = painterResource(R.drawable.default_avatar)

    val userUsername = LoggedUser.getUsername()
    val userBestScore = LoggedUser.getBestScore()
    val userAvatarImage = LoggedUser.getAvatarImage()

    if (userAvatarImage != null) imageResource = BitmapPainter(userAvatarImage.asImageBitmap())

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(R.string.profile),
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Row (
            horizontalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            Image(painter = imageResource, contentDescription = stringResource(R.string.avatar_image))

            Column (
                modifier = Modifier.padding(top = 16.dp)
            ){
                Text(text = stringResource(R.string.username) + ": $userUsername")

                Text(text = stringResource(R.string.best_score) + ": $userBestScore")
            }
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, SetAvatarActivity::class.java))
            },
        ) {
            Text(text = stringResource(R.string.set_avatar), fontSize = 20.sp)
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, ChangeUsernameActivity::class.java))
            },
        ) {
            Text(text = stringResource(R.string.change_username), fontSize = 20.sp)
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, ChangePasswordActivity::class.java))
            },
        ) {
            Text(text = stringResource(R.string.change_password), fontSize = 20.sp)
        }

        Row (
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    LoggedUser.logout()
                    context.startActivity(Intent(context, MainActivity::class.java))
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Text(text = stringResource(R.string.logout), fontSize = 20.sp)
            }

            Button(
                onClick = {
                    context.startActivity(Intent(context, DeleteProfileActivity::class.java))
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = stringResource(R.string.delete_profile), fontSize = 20.sp)
            }
        }
    }
}
