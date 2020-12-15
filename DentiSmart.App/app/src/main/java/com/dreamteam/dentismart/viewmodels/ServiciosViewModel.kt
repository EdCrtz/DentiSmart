package com.dreamteam.dentismart.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dreamteam.dentismart.models.Servicio
import com.dreamteam.dentismart.repositories.ServiciosRepository

class ServiciosViewModel :ViewModel() {
        private val repository: ServiciosRepository=ServiciosRepository()
        private var servicios: MutableLiveData<List<Servicio>> = repository.serviciosResponse
        fun getServicios(id: String): MutableLiveData<List<Servicio>> {
            repository.getServicios(id)
            return  servicios
        }
    }