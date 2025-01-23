package com.example.petpal.models


data class PetUpdateModel(
    val name: String?,
    val type: String?,
    val sex: String,
    val age: Int?
)
