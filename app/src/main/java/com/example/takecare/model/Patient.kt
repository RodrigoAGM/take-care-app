package com.example.takecare.model

data class Patient(
    val name: String,
    val lastName: String,
    val age: Int,
    val gender: String,
    val mail: String,
    val password: String,
    val userName: String,
    val birthday: String,
    val height: Double,
    val weight: Double
) {
}