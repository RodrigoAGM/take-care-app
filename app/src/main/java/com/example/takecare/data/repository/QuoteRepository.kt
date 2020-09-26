package com.example.takecare.data.repository

import com.example.takecare.data.TakeCareClient
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.response.GetQuotesResponse

class QuoteRepository{

    suspend fun get(): OperationResult<GetQuotesResponse> {
        try {
            val response = TakeCareClient.build().getQuotes()
            response.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else{
                    OperationResult.Error(Exception("Error al obtener las citas."))
                }
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }
}