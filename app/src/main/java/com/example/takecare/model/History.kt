package com.example.takecare.model

data class History (
    val patient: Patient,
    val level: Level,
    val frequency: Frequency,
    val description: String
){
}