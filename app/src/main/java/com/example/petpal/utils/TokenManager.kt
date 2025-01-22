package com.example.petpal.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("petpal_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "jwt_token"
    }

    // Save the token
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    // Retrieve the token
    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    // Clear the token
    fun clearToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }
}
