package com.example.takecare.utils

import com.example.takecare.model.Patient
import com.google.gson.Gson

object PatientUtil{

    lateinit var patient: Patient

    fun init(patientData: String) {
        patient = Gson().fromJson(patientData, Patient::class.java)
    }

}