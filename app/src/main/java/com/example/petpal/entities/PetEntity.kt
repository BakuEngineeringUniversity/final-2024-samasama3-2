package com.example.petpal.entities

data class PetEntity(
    val name: String,
    val type: String,
    val sex: String,
    val age: Int,
    val userId: Long? = null
)

