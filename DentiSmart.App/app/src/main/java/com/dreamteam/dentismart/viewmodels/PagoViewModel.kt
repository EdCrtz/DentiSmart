package com.dreamteam.dentismart.viewmodels

import androidx.lifecycle.MutableLiveData
import com.dreamteam.dentismart.models.Pago
import com.dreamteam.dentismart.repositories.PagoRepository

class PagoViewModel{
    private val repository: PagoRepository = PagoRepository()
    private var pagos: MutableLiveData<List<Pago>> = repository.pagoResponse
    private var pago: MutableLiveData<Pago> = repository.pagoCreateResponse
    val messageResponse: MutableLiveData<String>
    fun getPagos(): MutableLiveData<List<Pago>> {
        repository.getPagos()
        return  pagos
    }
    fun getPagosPaciente(id: String): MutableLiveData<List<Pago>> {
        repository.getPagosByPaciente(id)
        return  pagos
    }
    fun CreatePago(createPago: Pago): MutableLiveData<Pago> {
       // ((repository.Crea(createPago)
        return  pago
    }
    init {
        messageResponse = repository.messageResponse
    }
}