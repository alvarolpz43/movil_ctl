package com.example.movil_ctl.data.dataui

data class Pregunta(
    val id: String,
    val texto: String,
    val tipo: TipoPregunta,
    val opciones: List<String>? = null
)
