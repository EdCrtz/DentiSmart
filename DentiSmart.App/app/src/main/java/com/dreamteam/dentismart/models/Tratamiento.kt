package com.dreamteam.dentismart.models

import java.io.Serializable

data class Tratamiento(
    val cita: Cita,
    val detalle: List<Detalle>,
    val fechaCreacion: String,
    val id: String,
    val odontograma: Odontograma
) : Serializable