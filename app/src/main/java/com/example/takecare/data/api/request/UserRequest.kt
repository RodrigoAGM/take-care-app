package com.example.takecare.data.api.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    val last_name: String,
    val mail: String,
    val birthday: String,
    val password: String,
    val username: String
)

data class UpdateRequest(
    val name: String,
    val last_name: String,
    val gender: Int?,
    val mail: String,
    val birthday: String,
    val height: Double?,
    val weight: Double?,
    val image_url: String?
)

data class UpdatePasswordRequest(
    val password: String,
    val oldPassword: String
)