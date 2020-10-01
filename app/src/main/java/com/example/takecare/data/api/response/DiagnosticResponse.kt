package com.example.takecare.data.api.response

import com.example.takecare.model.Diagnostic
import com.example.takecare.model.Frequency
import com.example.takecare.model.Level
import com.google.gson.annotations.SerializedName

data class GetDiagnosticsResponse(
    val success: Boolean,
    @SerializedName("data")
    val diagnostics: List<Diagnostic>
)

data class AddDiagnosticResponse(
    val success: Boolean,
    @SerializedName("data")
    val diagnostic: Diagnostic,
    val frequency: Frequency,
    val level: Level
)