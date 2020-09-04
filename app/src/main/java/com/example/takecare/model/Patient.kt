package com.example.takecare.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Patient(
    @SerializedName("name")
    var name: String,
    @SerializedName("last_name")
    var lastName: String,
    var age: Int?,
    var gender: Int?,
    var mail: String,
    @SerializedName("username")
    var username: String,
    var birthday: String,
    var height: Double?,
    var weight: Double?,
    @SerializedName("image_url")
    var imageUrl: String?
): Serializable