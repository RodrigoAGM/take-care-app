package com.example.takecare.data.repository

import com.example.takecare.data.TakeCareClient
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.request.AddDiagnosticRequest
import com.example.takecare.data.api.response.AddDiagnosticResponse
import com.example.takecare.data.api.response.GetDiagnosticsResponse
import com.example.takecare.model.Frequency

class DiagnosticRepository {

    suspend fun get(): OperationResult<GetDiagnosticsResponse> {
        try {
            val response = TakeCareClient.build().getDiagnostics()
            response.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else{
                    OperationResult.Error(Exception("Error al obtener los diagnósticos."))
                }
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }

    suspend fun add(frequency: Frequency, date: String, description:String): OperationResult<AddDiagnosticResponse> {
        try {
            val addDiagnosticRequest = AddDiagnosticRequest(frequency, date, description)
            val response = TakeCareClient.build().addDiagnostic(addDiagnosticRequest)
            response.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else{
                    OperationResult.Error(Exception("Error al agregar el diagnóstico."))
                }
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }
}