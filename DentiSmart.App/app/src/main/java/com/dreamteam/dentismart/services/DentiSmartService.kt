package com.dreamteam.dentismart.services
import com.dreamteam.dentismart.models.Consultorio
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface DentiSmartService {
    @GET("Consultorio")
    fun getConsultorio(): Call<List<Consultorio>>

}