package com.dreamteam.dentismart.models

import java.io.Serializable

data class Usuario(
        val apellidoMat: String,
        val apellidoPat: String,
        val consultorio: String,
        val contrasenia: String,
        val datosConsultorio: Any,
        val email: String,
        val foto: String,
        val id: String,
        val nombre: String,
        val nombreUsuario: String,
        val horaFin: Any,
        val horaInicio: Any,
        val role: String,
        val telefono: String
):Serializable{
    override fun toString(): String = ("$nombre $apellidoPat $apellidoMat")
}