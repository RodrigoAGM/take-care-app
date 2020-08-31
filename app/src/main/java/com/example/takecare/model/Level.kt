package com.example.takecare.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Level (
    val name: String,
    val description: String,
    @SerializedName("max_frequency")
    val maxRate: Int,
    @SerializedName("min_frequency")
    val minRate: Int
):Serializable