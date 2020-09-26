package com.example.takecare.data.repository

import com.example.takecare.data.TakeCareClient
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.response.GetTreatmentResponse

class TreatmentRepository {

    suspend fun get(): OperationResult<GetTreatmentResponse> {
        try {
            val response = TakeCareClient.build().getTreatments()
            response.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else{
                    OperationResult.Error(Exception("Error al obtener las tratamientos."))
                }
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }
}