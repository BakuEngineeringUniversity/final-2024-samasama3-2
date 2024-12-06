package com.example.petpal.entities

data class UserEntity(
    var id: Long = 0,
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var phoneNumber: String,
    var address: String,
    var pets: List<PetEntity> = emptyList()
)
