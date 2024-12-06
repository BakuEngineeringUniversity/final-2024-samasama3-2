package com.example.petpal.entities

import java.time.LocalDateTime

data class CommonEntity(
    val id: Long = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

