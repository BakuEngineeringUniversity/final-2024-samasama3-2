package com.example.petpal.dtos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PetCreateDto(
    val name: String,
    val type: String,
    val sex: String,
    val age: Int,
    val userId: Long
) : Parcelable
