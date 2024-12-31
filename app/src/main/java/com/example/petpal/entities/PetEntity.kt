package com.example.petpal.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PetEntity(
    var name: String,
    var type: String,
    var sex: String,
    var age: Int,
    var userId: Long? = null
) : Parcelable
