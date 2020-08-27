package com.example.takecare.data.api.response

import com.example.takecare.model.Patient
import com.google.gson.annotations.SerializedName


/**
 * Created by Enzo Lizama Paredes on 8/26/20.
 * Contact: lizama.enzo@gmail.com
 */


data class LoginResponse(
    val success: Boolean,
    @SerializedName("user")
    val patient: Patient,
    val token: String,
    val refreshToken: String
)