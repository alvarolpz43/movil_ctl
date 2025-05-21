package com.example.movil_ctl.data.dataui

data class DynamicFormUiState(
    val tipoEquipo: String = "",
    val equipoEnUso: EstadoEquipo? = null,
    val preguntasOperativas: List<Pregunta> = emptyList(),
    val preguntasNoOperativas: List<Pregunta> = emptyList(),
    val respuestas: Map<String, String> = emptyMap(),
    val paradasAdicionales: List<Parada> = emptyList()
)
