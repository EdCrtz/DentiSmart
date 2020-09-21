package com.dreamteam.dentismart.models

data class Consultorio(
    val direccioConsultorio: DireccioConsultorio,
    val id: String,
    val nombre: String,
    val telefonos: List<Telefono>
)