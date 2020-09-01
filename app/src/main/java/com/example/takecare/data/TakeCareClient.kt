package com.example.takecare.data

import android.util.Log
import com.example.takecare.data.api.request.RefreshTokenRequest
import com.example.takecare.data.service.TakeCareService
import com.example.takecare.utils.PreferenceHelper
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import java.util.concurrent.TimeUnit


object TakeCareClient {
    private const val BASE_URL = "https://takecareapi.herokuapp.com"
    private lateinit var takeCareService: TakeCareService

    fun build(): TakeCareService {
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val httpClient = OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(TokenHeader())
            .addInterceptor(interceptor())
            .authenticator(TokenAuthenticator())

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

    private var mCounter = 0;

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = PreferenceHelper.refreshToken
        var updatedToken = ""
        if(mCounter > 0){
            return null
        }else{
            mCounter += 1
            if(!refreshToken.isNullOrBlank()){
                val authTokenResponse = TakeCareClient.build().refreshToken(RefreshTokenRequest(refreshToken)).execute().body()!!
                updatedToken = authTokenResponse.accessToken

                if(!updatedToken.isBlank()){
                    PreferenceHelper.token = "Bearer $updatedToken"
                }else{
                    return null
                }
            }else{
                return null
            }
        }

        return response.request.newBuilder()
            .header("Authorization", "Bearer $updatedToken")
            .method(response.request.method, response.request.body)
            .build()
    }

    private fun getRefreshedToken(): String {
        val refreshToken = PreferenceHelper.refreshToken
        var newToken = ""

        if(!refreshToken.isNullOrBlank()){
            val authTokenResponse = TakeCareClient.build().refreshToken(RefreshTokenRequest(refreshToken)).execute().body()!!
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