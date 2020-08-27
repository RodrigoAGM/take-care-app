package com.example.takecare.data.repository

import com.example.takecare.data.TakeCareClient
import com.example.takecare.data.api.OperationResult
import com.example.takecare.data.api.request.LoginRequest
import com.example.takecare.data.api.response.LoginResponse


/**
 * Created by Enzo Lizama Paredes on 8/26/20.
 * Contact: lizama.enzo@gmail.com
 */

class LoginRepository {
    suspend fun login(username: String, password: String): OperationResult<LoginResponse> {
        try {
            val response = TakeCareClient.build().login(LoginRequest(username, password))
            response.let {
                return if (it.isSuccessful && it.body() != null) {
                    val data = it.body()
                    OperationResult.Success(data)
                } else {
                    OperationResult.Error(Exception("Error al iniciar sesión"))
                }
            }
        } catch (e: Exception) {
            return OperationResult.Error(e)
        }
    }
}
