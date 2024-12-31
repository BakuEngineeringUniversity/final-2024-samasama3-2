package com.example.petpal.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var phoneNumber: String,
    var address: String,
    var pets: MutableList<PetEntity> = mutableListOf()
) : Parcelable
