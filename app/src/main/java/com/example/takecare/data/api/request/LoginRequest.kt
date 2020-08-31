package com.example.takecare.data.api.request

import com.example.takecare.model.Patient


data class LoginRequest(val username: String, val password: String)

data class RefreshTokenRequest (val token: String)

data class RequestRecoverPasswordRequest(val mail: String)

data class RecoverPasswordRequest(
    val mail: String,
    val password: String,
    val token: String
)
