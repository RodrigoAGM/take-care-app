package com.example.takecare.data

import com.example.takecare.data.service.TakeCareService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object TakeCareClient {
    private const val BASE_URL = "https://takecareapi.herokuapp.com"
    private lateinit var takeCareService: TakeCareService

    fun build(): TakeCareService {
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val httpClient = OkHttpClient.Builder().addInterceptor(interceptor())

        val retrofit: Retrofit = builder.client(httpClient.build()).build()
        takeCareService = retrofit.create(TakeCareService::class.java)
        return takeCareService


    }

    private fun interceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}