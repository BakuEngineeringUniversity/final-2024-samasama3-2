package com.example.petpal.dtos

import android.os.Parcelable


@Parcelize
data class LoginUserDto(
    val email: String,
    val password: String
) : Parcelable
