package com.example.doggame.datamanager

import android.content.Context
import android.util.Log
import java.util.regex.Matcher
import java.util.regex.Pattern

class Validation {
    private val passwordPattern: Pattern =
        Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}")

    fun isPasswordValid(password: String): Boolean {
        val matcher: Matcher = passwordPattern.matcher(password)
        return matcher.find()
    }

    fun checkRequiredFields(username: String, password: String, repeatedPassword: String): Boolean {
        return (username == "" || password == "" || repeatedPassword == "")
    }

    fun isUsernameUnique(username: String, context: Context): Boolean {
        val db = AppDatabase.getInstance(context)
        val userDao = db.userDAO()
        var user: User? = null

        try {
            user = userDao.findByUsername(username)
        } catch (e: Exception) {
            Log.e("DatabaseError", "Error reading from database", e)
        }

        return user == null
    }

    fun arePasswordsDifferent(password: String, repeatedPassword: String): Boolean {
        return password != repeatedPassword
    }
}
