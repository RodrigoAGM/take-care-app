package com.example.takecare.data.api.response

import com.example.takecare.model.Treatment
import com.google.gson.annotations.SerializedName

data class GetTreatmentResponse(
    val success: Boolean,
    @SerializedName("data")
    val treatments: List<Treatment>
)