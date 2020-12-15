package com.dreamteam.dentismart.models

import java.io.Serializable

data class Cita(
        val id: String? = null,
        val paciente:Usuario,
        val dentista:Usuario,
        val fechaCita:String,
        val fechaRegistro:String? = null,
        val servicios: List<Servicio>,
        val status:String,
        val pagada: Boolean = false,
        val tratamiento: String? = null
): Serializable
