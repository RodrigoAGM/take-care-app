package com.example.takecare.data.service

import com.example.takecare.data.api.request.LoginRequest
import com.example.takecare.data.api.request.RefreshTokenRequest
import com.example.takecare.data.api.response.LoginResponse
import com.example.takecare.data.api.response.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface TakeCareService {

    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/auth/refresh")
    fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<RefreshTokenResponse>

    @GET("/admin/users/add")
    suspend fun addUser(@Header("Authorization") token: String)
}