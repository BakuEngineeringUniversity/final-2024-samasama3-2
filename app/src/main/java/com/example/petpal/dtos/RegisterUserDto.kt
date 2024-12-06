package com.example.petpal.dtos

data class RegisterUserDto(
    val email: String,
    val firstName: String,
    val surname: String,
    val password: String,
    val phoneNumber: String,
    val address: String,
    val name: String,
    val sex: String,
    val type: String,
    val age: Int
)