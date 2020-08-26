package com.example.takecare.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Patient(
    @SerializedName("name")
    val name: String,
    @SerializedName("last_name")
    val lastName: String,
    val age: Int,
    val gender: String?,
    val mail: String,
    @SerializedName("username")
    val username: String,
    val birthday: String,
    val height: Double?,
    val weight: Double?
): Serializable