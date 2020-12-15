package com.dreamteam.dentismart.models

data class LoginResult(
    val apellidoMat: String,
    val apellidoPat: String,
    val consultorio: Any,
    val contrasenia: String,
    val datosConsultorio: Any,
    val email: String,
    val foto: String,
    val horaFin: Any,
    val horaInicio: Any,
    val id: String,
    val idFacebook: Any,
    val idGoogle: String,
    val nombre: String,
    val nombreUsuario: String,
    val refreshToken: Any,
    val role: String,
    val telefono: String,
    val token: String
)