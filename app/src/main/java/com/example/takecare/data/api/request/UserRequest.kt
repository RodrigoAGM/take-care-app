package com.example.takecare.data.api.request

data class RegisterRequest(
    val names: String,
    val last_name: String,
    val mail: String,
    val birthday: String,
    val password: String
)