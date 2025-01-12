package com.example.doggame.datamanager

import android.graphics.Bitmap
import android.graphics.BitmapFactory


object LoggedUser {
    private var username: String? = null
    private var password: String? = null
    private var bestScore: Int? = null
    private var avatarImage: Bitmap? = null
    private var lastScore: Int? = null

    fun login(user: User) {
        this.username = user.username
        this.password = user.password
        this.bestScore = user.bestScore
        this.avatarImage = user.avatarImage?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    }

    fun logout() {
        username = null
        password = null
        bestScore = null
        avatarImage = null
        lastScore = null
    }

    fun getUsername(): String? {return username}

    fun getPassword(): String? {return password}

    fun getBestScore(): Int { return bestScore ?: 0 }

    fun getAvatarImage(): Bitmap? {return avatarImage}

    fun getLastScore(): Int? {return lastScore}

    fun setUsername(username: String) {this.username = username}

    fun setPassword(password: String) {this.password = password}

    fun setAvatarImage(avatarImage: Bitmap) {this.avatarImage = avatarImage}

    fun setBestScore(bestScore: Int) {this.bestScore = bestScore}

    fun setLastScore(score: Int) {this.lastScore = score}
}
