package com.example.takecare.data

import android.content.SharedPreferences
import com.example.takecare.data.api.request.RefreshTokenRequest
import com.example.takecare.data.service.TakeCareService
import com.example.takecare.utils.PreferenceHelper
import okhttp3.*
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

        val httpClient = OkHttpClient.Builder()
            .authenticator(TokenAuthenticator())
            .addInterceptor(TokenHeader())
            .addInterceptor(interceptor())

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

class TokenAuthenticator: Authenticator{

    override fun authenticate(route: Route?, response: Response): Request? {
        val updatedToken = getRefreshedToken()
        return response.request.newBuilder()
            .header("Authorization", updatedToken)
            .method(response.request.method, response.request.body)
            .build()
    }

    private fun getRefreshedToken(): String {
        val refreshToken = PreferenceHelper.refreshToken
        var newToken = ""

        if(!refreshToken.isNullOrBlank()){
            val authTokenResponse = TakeCareClient.build().refreshToken(RefreshTokenRequest(refreshToken)).body()!!
            newToken = authTokenResponse.accessToken

            if(!newToken.isBlank()){
                PreferenceHelper.token = "Bearer $newToken"
            }
        }

        return newToken
    }
}

class TokenHeader : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {

        val token = PreferenceHelper.token
        val original = chain.request()
        var newRequest = original

        if(!token.isNullOrBlank()){
            val builder = original.newBuilder()
                .addHeader("Authorization", token)
                .method(original.method, original.body)

            newRequest = builder.build()
        }

        return chain.proceed(newRequest)
    }
}