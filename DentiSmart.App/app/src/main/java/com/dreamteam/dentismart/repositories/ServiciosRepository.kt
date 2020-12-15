package com.dreamteam.dentismart.repositories

import androidx.lifecycle.MutableLiveData
import com.dreamteam.dentismart.models.Servicio
import com.dreamteam.dentismart.services.DentiSmartService
import com.dreamteam.dentismart.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiciosRepository {
    private var dentismartService: DentiSmartService = RetrofitService.dentiSmartService
    var serviciosResponse: MutableLiveData<List<Servicio>> = MutableLiveData<List<Servicio>>()

    fun getServicios(id: String): MutableLiveData<List<Servicio>> {
        val call: Call<List<Servicio>> = this.dentismartService.getServicios(id)
        call.enqueue(object : Callback<List<Servicio>> {
            override fun onFailure(call: Call<List<Servicio>>?, t: Throwable?) {
                // TO DO
            }

            override fun onResponse(
                call: Call<List<Servicio>>?,
                response: Response<List<Servicio>>?
            ) {
                if (response!!.isSuccessful) {
                    this@ServiciosRepository.serviciosResponse.value = response.body()
                }
            }
        })
        return serviciosResponse
    }
}