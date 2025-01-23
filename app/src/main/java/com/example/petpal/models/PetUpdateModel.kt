package com.example.petpal.models

import com.example.petpal.enums.Sex

data class PetUpdateModel(
    val name: String?,
    val type: String?,
    val sex: Sex,
    val age: Int?
)
