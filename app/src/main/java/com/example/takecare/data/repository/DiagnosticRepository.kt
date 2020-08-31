package com.example.takecare.data.repository

import com.example.takecare.data.TakeCareClient
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.response.GetDiagnosticsResponse

class DiagnosticRepository {

    suspend fun get(): OperationResult<GetDiagnosticsResponse> {
        try {
            val response = TakeCareClient.build().getDiagnostics()
            response.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else{
                    OperationResult.Error(Exception("Error al obtener los diagnosticos."))
                }
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }
}