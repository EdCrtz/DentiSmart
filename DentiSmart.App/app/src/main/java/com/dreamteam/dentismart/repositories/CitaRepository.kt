package com.dreamteam.dentismart.repositories

import androidx.lifecycle.MutableLiveData
import com.dreamteam.dentismart.models.CaseResponse
import com.dreamteam.dentismart.models.Cita
import com.dreamteam.dentismart.models.ServiceResponse
import com.dreamteam.dentismart.services.DentiSmartService
import com.dreamteam.dentismart.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitaRepository {
    private var dentismartService: DentiSmartService = RetrofitService.dentiSmartService
    var citaResponse: MutableLiveData<List<Cita>> = MutableLiveData<List<Cita>>()
    var citaCreateResponse: MutableLiveData<Cita> = MutableLiveData<Cita>()
    var serviceResponse: MutableLiveData<ServiceResponse> = MutableLiveData<ServiceResponse>()
    var messageResponse = MutableLiveData<String>()
    var caseResponse = MutableLiveData<CaseResponse>()
    fun getCitas(): MutableLiveData<List<Cita>> {
        val call: Call<List<Cita>> = this.dentismartService.getCitas()
        call.enqueue(object : Callback<List<Cita>> {
            override fun onFailure(call: Call<List<Cita>>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                    call: Call<List<Cita>>?,
                    response: Response<List<Cita>>?
            ) {
                if (response!!.isSuccessful) {
                    this@CitaRepository.citaResponse.value = response.body()
                }
            }
        })
        return citaResponse
    }
    fun CreateCitas(cita:Cita): MutableLiveData<Cita> {
        val call: Call<Cita> = this.dentismartService.createCitas(cita)
        call.enqueue(object : Callback<Cita> {
            override fun onFailure(call: Call<Cita>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                    call: Call<Cita>?,
                    response: Response<Cita>?
            ) {
                if (response!!.isSuccessful) {
                    this@CitaRepository.citaCreateResponse.value = response.body()
                }
            }
        })
        return citaCreateResponse
    }

    fun UpdateCitas(cita:Cita): MutableLiveData<CaseResponse> {
        val call: Call<Void> = this.dentismartService.updateCitas(cita)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                call: Call<Void>?,
                response: Response<Void>?
            ) {
                if (response!!.isSuccessful) {
                    caseResponse.value = CaseResponse(1, "Se actualizo correctamente", true)
                }
            }
        })
        return caseResponse
    }
    fun DeleteCita(id:String): MutableLiveData<CaseResponse> {
        val call: Call<Void> = this.dentismartService.deleteCitasById(id)
        call.enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                // TO DO
            }
            override fun onResponse(
                call: Call<Void>?,
                response: Response<Void>?
            ) {
                if (response!!.isSuccessful) {
                    caseResponse.value = CaseResponse(2, "Se cancelo la cita correctamente", true)
                }
            }
        })
        return caseResponse
    }
    fun checkAvailabilityCitas(cita:Cita): MutableLiveData<ServiceResponse> {
        val call: Call<ServiceResponse> = this.dentismartService.checkAvailabilityCitas(cita)
        call.enqueue(object : Callback<ServiceResponse> {
            override fun onFailure(call: Call<ServiceResponse>?, t: Throwable?) {
                messageResponse.value ="No hay horario disponible"
            }
            override fun onResponse(
                call: Call<ServiceResponse>?,
                response: Response<ServiceResponse>?
            ) {
                if (response!!.isSuccessful) {
                    this@CitaRepository.serviceResponse.value = response.body()
                }
            }
        })
        return serviceResponse
    }


    fun getCitasByDentista(dentista: String): MutableLiveData<List<Cita>> {
        val call: Call<List<Cita>> = this.dentismartService.getCitasByIdDentista(dentista)
        call.enqueue(object : Callback<List<Cita>> {
            override fun onFailure(call: Call<List<Cita>>?, t: Throwable?) {
                // TO DO
            }

            override fun onResponse(
                call: Call<List<Cita>>?,
                response: Response<List<Cita>>?
            ) {
                if (response!!.isSuccessful) {
                    this@CitaRepository.citaResponse.value = response.body()
                }
            }
        })
        return citaResponse
    }
    fun getCitasByPaciente(paciente: String): MutableLiveData<List<Cita>> {
        val call: Call<List<Cita>> = this.dentismartService.getCitasByIdPaciente(paciente)
        call.enqueue(object : Callback<List<Cita>> {
            override fun onFailure(call: Call<List<Cita>>?, t: Throwable?) {
                // TO DO
            }

            override fun onResponse(
                call: Call<List<Cita>>?,
                response: Response<List<Cita>>?
            ) {
                if (response!!.isSuccessful) {
                    this@CitaRepository.citaResponse.value = response.body()
                }
            }
        })
        return citaResponse
    }

    fun getCitasByConsultorio(consultorio: String): MutableLiveData<List<Cita>> {
        val call: Call<List<Cita>> = this.dentismartService.getCitasByIdConsultorio(consultorio)
        call.enqueue(object : Callback<List<Cita>> {
            override fun onFailure(call: Call<List<Cita>>?, t: Throwable?) {
                // TO DO
            }

            override fun onResponse(
                call: Call<List<Cita>>?,
                response: Response<List<Cita>>?
            ) {
                if (response!!.isSuccessful) {
                    this@CitaRepository.citaResponse.value = response.body()
                }
            }
        })
        return citaResponse
    }
}