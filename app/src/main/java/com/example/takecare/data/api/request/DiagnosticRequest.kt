package com.example.takecare.data.api.request

import com.example.takecare.model.Frequency

data class AddDiagnosticRequest(
    val frequency: Frequency,
    val date: String,
    val description: String
)