package com.dreamteam.dentismart.models

data class Consultorio(
    val direccionConsultorio: DireccionConsultorio,
    val id: String,
    val nombre: String,
    val telefonos: List<Telefono>
){
    override fun toString(): String = nombre
}