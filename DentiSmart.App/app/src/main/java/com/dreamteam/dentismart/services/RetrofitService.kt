package com.dreamteam.dentismart.services

import com.dreamteam.dentismart.environments.Environment
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass


class RetrofitService {
    private val okHttpClient = OkHttpClient().newBuilder()
        .retryOnConnectionFailure(true)
        .connectTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .build()
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Environment.API)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun <S> createService(serviceClass: KClass<DentiSmartService>): S {
        return retrofit.create(serviceClass)
    }
}