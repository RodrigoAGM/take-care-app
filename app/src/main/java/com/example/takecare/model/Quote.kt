package com.example.takecare.model

import com.google.gson.annotations.SerializedName

data class Quote (

    @SerializedName("appointment_date")
    var appointmentDate: String,
    @SerializedName("appointment_time")
    var appointmentTime: String,
    var status: Int,
    @SerializedName("name")
    var psychiatristName: String,
    @SerializedName("last_name")
    var psychiatristLastName: String
)