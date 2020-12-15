package com.dreamteam.dentismart.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dreamteam.dentismart.models.CaseResponse
import com.dreamteam.dentismart.models.Cita
import com.dreamteam.dentismart.models.ServiceResponse
import com.dreamteam.dentismart.repositories.CitaRepository

class CitaViewModel: ViewModel() {
    private val repository: CitaRepository = CitaRepository()
    private var citas: MutableLiveData<List<Cita>> = repository.citaResponse
    var cita: MutableLiveData<Cita> = repository.citaCreateResponse
    val respuesta: MutableLiveData<ServiceResponse> = repository.serviceResponse
    val caseResponse: MutableLiveData<CaseResponse> = repository.caseResponse
    val messageResponse: MutableLiveData<String>
    fun getCitas(): MutableLiveData<List<Cita>> {
        repository.getCitas()
        return  citas
    }
    fun getCitasPaciente(id: String): MutableLiveData<List<Cita>> {
        repository.getCitasByPaciente(id)
        return  citas
    }
    fun CreateCitas(createcita:Cita): MutableLiveData<Cita> {
        repository.CreateCitas(createcita)
        return  cita
    }
    fun UpdateCitas(update:Cita){
        repository.UpdateCitas(update)
    }
    fun DeleteCitas(id: String){
        repository.DeleteCita(id)
    }
    fun checkAvailabilityCitas(checkcita:Cita)  {
        repository.checkAvailabilityCitas(checkcita)

    }
    init {
        messageResponse = repository.messageResponse
    }
}