package com.dreamteam.dentismart.models

import java.io.Serializable

data class Diente(
    val centro: Int,
    val derecho: Int,
    val estado: Int,
    val izquierdo: Int,
    val norte: Int,
    val sur: Int
) : Serializable