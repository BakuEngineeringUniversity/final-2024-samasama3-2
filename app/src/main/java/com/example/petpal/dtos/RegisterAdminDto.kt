package com.example.petpal.dtos

data class RegisterAdminDto(
    val email: String,
    val firstName: String,
    val surname: String,
    val password: String,
    val phoneNumber: String,
    val address: String
)
