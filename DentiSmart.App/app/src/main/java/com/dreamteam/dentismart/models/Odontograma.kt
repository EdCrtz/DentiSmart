package com.dreamteam.dentismart.models

import java.io.Serializable

data class Odontograma(
    val dientesInferior: List<Diente>,
    val dientesSuperior: List<Diente>
) : Serializable