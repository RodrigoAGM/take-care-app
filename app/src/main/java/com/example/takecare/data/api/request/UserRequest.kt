package com.example.takecare.data.api.request

data class RegisterRequest(
    val name: String,
    val last_name: String,
    val mail: String,
    val birthday: String,
    val password: String,
    val username: String
)