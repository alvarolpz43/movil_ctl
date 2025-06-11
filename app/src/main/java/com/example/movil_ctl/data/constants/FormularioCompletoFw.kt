package com.example.movil_ctl.data.constants

import java.util.Date

data class FormularioCompletoFw(
    val serieEquipo: String,
    val nombreOperador: String,
    val cedulaOperador: String,
    val zona: String,
    val nombreContratista: String,
    val fecha: Date,
    val finca: String,
    val especie: String,
    val lote: String,
    val turno: String,
    val pendiente: Int,
    val numeroCargas: Int,
    val viajesHora: Int,
    val metrosCubicos: Double,
    val pesoCargas: Double,
    val distancia: Double,
    val tProgramado: Double,
    val tParadaMecanica: Double,
    val especificacionParadaM: String,
    val refRespuesto: String,
    val tPorAlistamiento: Double,
    val tanqueo: Double,
    val alimentacion: Double,
    val tUsoWinche: Double,
    val novedades: String,
    val clima: String,
    val hSueloSaturado: Double,
    val hParadas: Double,
    val totrasParadas: List<String>,
    val motivoOtrasParadas: List<String>
    )
