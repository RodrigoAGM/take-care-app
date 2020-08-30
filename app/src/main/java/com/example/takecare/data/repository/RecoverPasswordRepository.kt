package com.example.takecare.data.repository

import com.example.takecare.data.TakeCareClient
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.request.LoginRequest
import com.example.takecare.data.api.request.RecoverPasswordRequest
import com.example.takecare.data.api.request.RequestRecoverPasswordRequest
import com.example.takecare.data.api.response.LoginResponse
import com.example.takecare.data.api.response.RecoverPasswordResponse

class RecoverPasswordRepository {

    suspend fun requestRecoverPassword(mail: String): OperationResult<RecoverPasswordResponse> {
        try {
            val response = TakeCareClient.build().requestRecoverPassword(
                RequestRecoverPasswordRequest(mail)
            )
            response.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else{
                    if (it.code() == 400){
                        OperationResult.Error(Exception("El email ingresado no está registrado."))
                    }else{
                        OperationResult.Error(Exception("Error al recuperar contraseña."))
                    }
                }
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }

    suspend fun recoverPassword(mail: String, password: String, token:String): OperationResult<RecoverPasswordResponse> {
        try {
            val response = TakeCareClient.build().recoverPassword(RecoverPasswordRequest(mail, password, token))
            response.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else{
                    if (it.code() == 400){
                        OperationResult.Error(Exception("El token de recuperación ingresado no es válido."))
                    }else{
                        OperationResult.Error(Exception("Error al recuperar contraseña."))
                    }
                }
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }
}