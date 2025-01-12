package com.example.doggame.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


object AvatarService {
    private const val URL = "https://robohash.org/"
    private const val URL_SET = "?set=set"

    private val optionToNumberMap = mapOf("Robot" to 1, "Monster" to 2,
        "Robot Head" to 3, "Kitten" to 4)

    suspend fun getAvatar(selectedOption: String): Bitmap? {
        val numberOption = optionToNumberMap[selectedOption]
        val randomNumber = (1..50).random()
        val url = URL + randomNumber + URL_SET + numberOption

        return makeApiCall(url)

    }

    private suspend fun makeApiCall(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()

            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.byteStream()?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }
                } else {
                    Log.e("AvatarServiceError", "Error with avatar request: ${response.code}")
                    null
                }
            } catch (e: Exception) {
                Log.e("AvatarServiceError", "Exception occurred: ${e.message}", e)
                null
            }
        }
    }
}
