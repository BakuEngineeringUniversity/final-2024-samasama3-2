package com.example.petpal.models

data class PetModel(
    val name: String,
    val type: String,
    val sex: String,
    val age: Int,
    val id: Long,
    val createdAt: String,
    val updatedAt: String
)

