package com.example.takecare.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Treatment (
    @SerializedName("creation_date")
    var creationDate: String,
    var status: Int,
    var indications: String,
    var quantity: Int,
    var frequency: Int,
    @SerializedName("name_psychiatrist")
    var psychiatristName: String,
    @SerializedName("last_name")
    var psychiatristLastName: String,
    @SerializedName("name_medicine")
    var medicineName: String,
    var description: String
) : Serializable
