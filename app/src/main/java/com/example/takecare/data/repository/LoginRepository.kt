package com.example.takecare.data.repository

import com.example.takecare.data.TakeCareClient
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.request.LoginRequest
import com.example.takecare.data.api.response.LoginResponse
import com.example.takecare.data.api.response.LogoutResponse


class LoginRepository {
    suspend fun login(username: String, password: String): OperationResult<LoginResponse> {
        try {
            val response = TakeCareClient.build().login(LoginRequest(username, password))
            response.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else{
                    if (it.code() == 400){
                        OperationResult.Error(Exception("Usuario o contraseña incorrecto"))
                    }else{
                        OperationResult.Error(Exception("Error al iniciar sesión"))
                    }
                }
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }

    suspend fun logout(): OperationResult<LogoutResponse> {
        try {
            val response = TakeCareClient.build().logout()
            response.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else{
                    OperationResult.Error(Exception("Error al cerrar sesión"))
                }
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }
}
