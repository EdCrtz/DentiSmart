package com.dreamteam.dentismart.models

import java.io.Serializable

data class Detalle(
    val evidencia: String,
    val observacion: String,
    val receta: String,
    val servicio: Servicio,
    val sintomas: String
) : Serializable