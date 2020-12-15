package com.dreamteam.dentismart.services

import com.dreamteam.dentismart.environments.Environment
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    private var okHttpClient = OkHttpClient().newBuilder()
        .retryOnConnectionFailure(true)
        .connectTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .build()
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()
    private var retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Environment.API)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    var dentiSmartService = retrofit.create(DentiSmartService::class.java)
    fun setToken(token: String) {
        this.okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }
            .retryOnConnectionFailure(true)
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build()
        this.retrofit = Retrofit.Builder().client(okHttpClient)
            .baseUrl(Environment.API)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        this.dentiSmartService = retrofit.create(DentiSmartService::class.java)
    }

}