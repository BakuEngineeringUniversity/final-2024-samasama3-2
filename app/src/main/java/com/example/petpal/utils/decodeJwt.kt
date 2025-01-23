package com.example.petpal.utils

import android.util.Base64
import org.json.JSONObject

fun decodeJwt(token: String): Long? {
    return try {
        val parts = token.split(".")
        if (parts.size == 3) {
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val json = JSONObject(payload)
            json.getLong("id") // Assuming `id` is part of the payload
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}