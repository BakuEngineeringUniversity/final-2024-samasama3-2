package com.example.petpal.utils

import android.content.Context

class TokenManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("PetPalPrefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }
}
