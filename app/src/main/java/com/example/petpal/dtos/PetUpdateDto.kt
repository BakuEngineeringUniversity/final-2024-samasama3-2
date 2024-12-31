package com.example.petpal.dtos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PetUpdateDto(
    val name: String? = null,
    val type: String? = null,
    val sex: String? = null,
    val age: Int? = null
) : Parcelable
