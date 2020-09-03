package com.example.takecare.data.api.response

import com.example.takecare.model.Patient
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val success: Boolean,
    @SerializedName("data")
    val patient: Patient,
    val id: String
)

data class UpdateResponse(
    val success: Boolean
)