package com.example.takecare.data.api.response

import com.example.takecare.model.Patient
import com.google.gson.annotations.SerializedName


data class LoginResponse(
    val success: Boolean,
    @SerializedName("user")
    val patient: Patient,
    val token: String,
    val refreshToken: String
)

data class RefreshTokenResponse(
    val success: Boolean,
    val accessToken: String
)

data class RecoverPasswordResponse(
    val success: Boolean,
    val message:String
)

data class LogoutResponse(
    val success: Boolean
)
