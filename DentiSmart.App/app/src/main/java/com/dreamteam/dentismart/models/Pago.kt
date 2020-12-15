package com.dreamteam.dentismart.models

data class Pago(
    val cita: Cita,
    val fechaPago: String,
    val id: String,
    val idUsuario: String,
    val metodo: Int,
    val monto: Int
)