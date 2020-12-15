package com.dreamteam.dentismart.repositories

import androidx.lifecycle.MutableLiveData
import com.dreamteam.dentismart.models.Consultorio
import com.dreamteam.dentismart.services.DentiSmartService
import com.dreamteam.dentismart.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultorioRepository {
    private var dentismartService: DentiSmartService = RetrofitService.dentiSmartService
    var consultorioResponse: MutableLiveData<List<Consultorio>> =
        MutableLiveData<List<Consultorio>>()

    fun getConsultorios(): MutableLiveData<List<Consultorio>> {
        val call: Call<List<Consultorio>> = this.dentismartService.getConsultorio()
        call.enqueue(object : Callback<List<Consultorio>> {
            override fun onFailure(call: Call<List<Consultorio>>?, t: Throwable?) {
                // TO DO
            }

            override fun onResponse(
                call: Call<List<Consultorio>>?,
                response: Response<List<Consultorio>>?
            ) {
                if (response!!.isSuccessful) {
                    this@ConsultorioRepository.consultorioResponse.value = response.body()
                }
            }
        })
        return consultorioResponse
    }
}