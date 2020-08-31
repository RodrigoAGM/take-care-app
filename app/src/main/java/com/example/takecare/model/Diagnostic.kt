package com.example.takecare.model

import java.io.Serializable

data class Diagnostic (
    val date: String,
    val description: String,
    val level: Level,
    val frequency: Frequency
):Serializable