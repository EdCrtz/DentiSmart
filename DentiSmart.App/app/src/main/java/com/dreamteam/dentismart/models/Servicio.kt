package com.dreamteam.dentismart.models

import java.io.Serializable

data class Servicio(
    val consultorio: String,
    val costoXDiente: Boolean,
    val descripcion: String,
    val id: String,
    val nombre: String,
    val precio: Int
):Serializable{
    override fun toString(): String = nombre
}