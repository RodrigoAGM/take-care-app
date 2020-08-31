package com.example.takecare.data.repository

import com.example.takecare.data.TakeCareClient
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.request.RegisterRequest
import com.example.takecare.data.api.response.RegisterResponse

class UserRepository {

    suspend fun register(name: String, last_name: String, mail: String, birthday: String,
                         password: String, username: String): OperationResult<RegisterResponse> {
        try {
            val response = TakeCareClient.build().register(RegisterRequest(
               name, last_name, mail, birthday, password, username))

            response.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else{
                    if (it.code() == 400){
                        OperationResult.Error(Exception("El mail o username ya está registrado."))
                    }else{
                        OperationResult.Error(Exception("Error al registrar."))
                    }
                }
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }
}