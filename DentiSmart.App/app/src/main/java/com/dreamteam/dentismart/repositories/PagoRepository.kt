package com.dreamteam.dentismart.repositories

import androidx.lifecycle.MutableLiveData
import com.dreamteam.dentismart.models.Pago
import com.dreamteam.dentismart.services.DentiSmartService
import com.dreamteam.dentismart.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagoRepository {
    private var dentismartService: DentiSmartService = RetrofitService.dentiSmartService
    var pagoResponse: MutableLiveData<List<Pago>> = MutableLiveData<List<Pago>>()
    var pagoCreateResponse: MutableLiveData<Pago> = MutableLiveData<Pago>()
    var messageResponse = MutableLiveData<String>()
    fun getPagos(): MutableLiveData<List<Pago>> {
        val call: Call<List<Pago>> = this.dentismartService.getPagos()
        call.enqueue(object : Callback<List<Pago>> {
            override fun onFailure(call: Call<List<Pago>>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                call: Call<List<Pago>>?,
                response: Response<List<Pago>>?
            ) {
                if (response!!.isSuccessful) {
                    this@PagoRepository.pagoResponse.value = response.body()
                }
            }
        })
        return pagoResponse
    }

    fun getPagosByDentista(dentista: String): MutableLiveData<List<Pago>> {
        val call: Call<List<Pago>> = this.dentismartService.getPagoByIdDentista(dentista)
        call.enqueue(object : Callback<List<Pago>> {
            override fun onFailure(call: Call<List<Pago>>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                call: Call<List<Pago>>?,
                response: Response<List<Pago>>?
            ) {
                if (response!!.isSuccessful) {
                    this@PagoRepository.pagoResponse.value = response.body()
                }
            }
        })
        return pagoResponse
    }
    fun getPagosByPaciente(paciente: String): MutableLiveData<List<Pago>> {
        val call: Call<List<Pago>> = this.dentismartService.getPagoByIdPaciente(paciente)
        call.enqueue(object : Callback<List<Pago>> {
            override fun onFailure(call: Call<List<Pago>>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                call: Call<List<Pago>>?,
                response: Response<List<Pago>>?
            ) {
                if (response!!.isSuccessful) {
                    this@PagoRepository.pagoResponse.value = response.body()
                }
            }
        })
        return pagoResponse
    }

    fun getPagosByConsultorio(consultorio: String): MutableLiveData<List<Pago>> {
        val call: Call<List<Pago>> = this.dentismartService.getPagoByIdConsultorio(consultorio)
        call.enqueue(object : Callback<List<Pago>> {
            override fun onFailure(call: Call<List<Pago>>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                call: Call<List<Pago>>?,
                response: Response<List<Pago>>?
            ) {
                if (response!!.isSuccessful) {
                    this@PagoRepository.pagoResponse.value = response.body()
                }
            }
        })
        return pagoResponse
    }
}