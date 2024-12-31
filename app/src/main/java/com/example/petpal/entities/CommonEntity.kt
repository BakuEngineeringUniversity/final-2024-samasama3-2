package com.example.petpal.entities

import java.text.SimpleDateFormat
import java.util.*

open class CommonEntity(
    val id: Long = 0,
    val createdAt: String = getCurrentTime(),
    var updatedAt: String = getCurrentTime()
) {
    companion object {
        private fun getCurrentTime(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            return dateFormat.format(Date())
        }
    }
}
