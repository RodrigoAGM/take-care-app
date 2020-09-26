package com.example.takecare.data.api.response

import com.example.takecare.model.Quote
import com.google.gson.annotations.SerializedName

data class GetQuotesResponse(
    val success: Boolean,
    @SerializedName("data")
    val quotes: List<Quote>
)