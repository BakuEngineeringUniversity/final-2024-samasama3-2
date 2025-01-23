package com.example.petpal.models

data class RegisterUserModel(
    val email: String,
    val firstName: String,
    val surname: String,
    val password: String,
    val phoneNumber: String,
    val address: String,
    val petName: String,
    val petType: String,
    val petSex: String,
    val petAge: Int
)
