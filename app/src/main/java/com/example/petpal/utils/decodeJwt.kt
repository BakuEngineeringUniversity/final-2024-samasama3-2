package com.example.petpal.utils

import android.util.Base64
import android.util.Log
import org.json.JSONObject

fun decodeJwt(token: String): Long? {
    return try {
        val parts = token.split(".")
        if (parts.size == 3) {
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            Log.d("decodeJwt", "Decoded JWT payload: $payload")
            val json = JSONObject(payload)
            json.getLong("id") // Ensure "id" exists in the JWT payload
        } else {
            Log.e("decodeJwt", "Invalid JWT token format")
            null
        }
    } catch (e: Exception) {
        Log.e("decodeJwt", "Error decoding JWT: ${e.message}")
        null
    }
}
