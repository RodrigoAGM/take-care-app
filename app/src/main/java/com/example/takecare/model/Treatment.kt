package com.example.takecare.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Treatment (
    @SerializedName("creation_date")
    var creationDate: String,
    var status: Int,
    var indications: String,
    @SerializedName("psychiatrist_name")
    var psychiatristName: String,
    @SerializedName("psychiatrist_last_name")
    var psychiatristLastName: String,
    var details : List<TreatmentDetail>
) : Serializable
