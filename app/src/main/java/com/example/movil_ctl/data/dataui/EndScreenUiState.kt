package com.example.movil_ctl.data.dataui

data class EndScreenUiState(
    val tipoEquipo: String = "",
    val preguntas: List<Pregunta> = emptyList(),
    val respuestas: Map<String, String> = emptyMap(),
)
