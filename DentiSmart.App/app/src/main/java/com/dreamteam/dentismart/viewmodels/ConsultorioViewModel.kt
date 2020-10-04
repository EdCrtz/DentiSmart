package com.dreamteam.dentismart.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dreamteam.dentismart.models.Consultorio
import com.dreamteam.dentismart.repositories.ConsultorioRepository


class ConsultorioViewModel:ViewModel()
{
    private val repository: ConsultorioRepository = ConsultorioRepository()
    private var consultorios: MutableLiveData<List<Consultorio>> = repository.consultorioResponse;
}