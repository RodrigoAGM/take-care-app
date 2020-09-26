package com.example.takecare.data.api.response

import com.example.takecare.model.Patient
import com.example.takecare.model.Quote
import com.example.takecare.model.Treatment
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

data class UpdatePasswordResponse(
    val success: Boolean,
    val message:String
)

data class GetTreatmentResponse(
    val success: Boolean,
    @SerializedName("data")
    val treatments: List<Treatment>
)

data class GetQuotesResponse(
    val success: Boolean,
    @SerializedName("data")
    val quotes: List<Quote>
)