package com.dreamteam.dentismart.repositories

import androidx.lifecycle.MutableLiveData
import com.dreamteam.dentismart.models.Tratamiento
import com.dreamteam.dentismart.services.DentiSmartService
import com.dreamteam.dentismart.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TratamientoRepository {
    private var dentismartService: DentiSmartService = RetrofitService.dentiSmartService
    var tratamientoResponse: MutableLiveData<List<Tratamiento>> = MutableLiveData<List<Tratamiento>>()
    var tratamientoCreateResponse: MutableLiveData<Tratamiento> = MutableLiveData<Tratamiento>()
    var messageResponse = MutableLiveData<String>()
    fun getTratamientos(): MutableLiveData<List<Tratamiento>> {
        val call: Call<List<Tratamiento>> = this.dentismartService.getTratamientos()
        call.enqueue(object : Callback<List<Tratamiento>> {
            override fun onFailure(call: Call<List<Tratamiento>>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                call: Call<List<Tratamiento>>?,
                response: Response<List<Tratamiento>>?
            ) {
                if (response!!.isSuccessful) {
                    this@TratamientoRepository.tratamientoResponse.value = response.body()
                }
            }
        })
        return tratamientoResponse
    }

    fun getTratamientosByDentista(dentista: String): MutableLiveData<List<Tratamiento>> {
        val call: Call<List<Tratamiento>> = this.dentismartService.getTratamientoByIdDentista(dentista)
        call.enqueue(object : Callback<List<Tratamiento>> {
            override fun onFailure(call: Call<List<Tratamiento>>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                call: Call<List<Tratamiento>>?,
                response: Response<List<Tratamiento>>?
            ) {
                if (response!!.isSuccessful) {
                    this@TratamientoRepository.tratamientoResponse.value = response.body()
                }
            }
        })
        return tratamientoResponse
    }
    fun getTratamientosByPaciente(paciente: String): MutableLiveData<List<Tratamiento>> {
        val call: Call<List<Tratamiento>> = this.dentismartService.getTratamientoByIdPaciente(paciente)
        call.enqueue(object : Callback<List<Tratamiento>> {
            override fun onFailure(call: Call<List<Tratamiento>>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                call: Call<List<Tratamiento>>?,
                response: Response<List<Tratamiento>>?
            ) {
                if (response!!.isSuccessful) {
                    this@TratamientoRepository.tratamientoResponse.value = response.body()
                }
            }
        })
        return tratamientoResponse
    }

    fun getTratamientosByConsultorio(consultorio: String): MutableLiveData<List<Tratamiento>> {
        val call: Call<List<Tratamiento>> = this.dentismartService.getTratamientoByIdConsultorio(consultorio)
        call.enqueue(object : Callback<List<Tratamiento>> {
            override fun onFailure(call: Call<List<Tratamiento>>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                call: Call<List<Tratamiento>>?,
                response: Response<List<Tratamiento>>?
            ) {
                if (response!!.isSuccessful) {
                    this@TratamientoRepository.tratamientoResponse.value = response.body()
                }
            }
        })
        return tratamientoResponse
    }
}