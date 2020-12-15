package com.dreamteam.dentismart.viewmodels

import androidx.lifecycle.MutableLiveData
import com.dreamteam.dentismart.models.Tratamiento
import com.dreamteam.dentismart.repositories.TratamientoRepository

class TratamientoViewModel {
    private val repository: TratamientoRepository = TratamientoRepository()
    private var tratamientos: MutableLiveData<List<Tratamiento>> = repository.tratamientoResponse
    private var tratamiento: MutableLiveData<Tratamiento> = repository.tratamientoCreateResponse
    val messageResponse: MutableLiveData<String>
    fun getTratamientos(): MutableLiveData<List<Tratamiento>> {
        repository.getTratamientos()
        return tratamientos
    }

    fun getTratamientosPaciente(id: String): MutableLiveData<List<Tratamiento>> {
        repository.getTratamientosByPaciente(id)
        return tratamientos
    }

    fun CreateTratamiento(createTratamiento: Tratamiento): MutableLiveData<Tratamiento> {
        // ((repository.Crea(createTratamiento)
        return tratamiento
    }

    init {
        messageResponse = repository.messageResponse
    }
}