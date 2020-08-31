package com.example.takecare.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Frequency (
    @SerializedName("heart_rate")
    val heartRate: Int,
    val date: String
): Serializable