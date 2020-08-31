package com.example.takecare.data.api.response

import com.example.takecare.model.Diagnostic
import com.google.gson.annotations.SerializedName

data class GetDiagnosticsResponse(
    val success: Boolean,
    @SerializedName("data")
    val diagnostics: List<Diagnostic>
)