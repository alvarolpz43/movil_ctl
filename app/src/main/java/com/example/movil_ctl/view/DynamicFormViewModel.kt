package com.example.movil_ctl.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.movil_ctl.data.constants.FormularioCompletoFw
import com.example.movil_ctl.data.constants.FormularioCompletoHv
import com.example.movil_ctl.data.constants.FormularioHolder
import com.example.movil_ctl.data.dataui.DynamicFormUiState
import com.example.movil_ctl.data.dataui.EstadoEquipo
import com.example.movil_ctl.data.dataui.Parada
import com.example.movil_ctl.data.dataui.Pregunta
import com.example.movil_ctl.data.dataui.TipoPregunta
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DynamicFormViewModel @Inject constructor(
    private val saved: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DynamicFormUiState())
    val uiState: StateFlow<DynamicFormUiState> = _uiState

    fun setTipoEquipo(tipo: String) {
        val preguntasSi = when (tipo.lowercase()) {
            "harvester" -> listOf(
                Pregunta("m3", "¿Producción en metros cúbicos (m³)?", TipoPregunta.NUMERO),
                Pregunta("diametro", "¿Diámetro medio del fuste (cm)?", TipoPregunta.NUMERO),
                Pregunta("pendiente", "¿Pendiente del terreno (en grados°)?", TipoPregunta.NUMERO),
                Pregunta("fustes_total", "¿N.º total de fustes?", TipoPregunta.NUMERO),
                Pregunta("m3_hora", "¿Productividad (m³/hora)?", TipoPregunta.NUMERO),
                Pregunta("fustes_hora", "¿Cantidad de fustes por hora?", TipoPregunta.NUMERO),
                Pregunta("tiempo_programado", "¿Tiempo programado (h)?", TipoPregunta.NUMERO),
                Pregunta("alistamiento", "¿Tiempo en alistamiento (minutos)?", TipoPregunta.NUMERO),
                Pregunta("tanqueo", "¿Tiempo de tanqueo (minutos)?", TipoPregunta.NUMERO),
                Pregunta("alimentacion", "¿Tiempo de alimentación (minutos)?", TipoPregunta.NUMERO),
                Pregunta(
                    "paradas_mecanicas",
                    "¿Tiempo de paradas mecánicas (minutos)?",
                    TipoPregunta.NUMERO
                ),
                Pregunta(
                    "tEspecificado",
                    "Especificar parada mecanica",
                    TipoPregunta.SELECCION,
                    listOf("en reparacion", "esperando reparacion")
                ),
                Pregunta("repuesto", "¿Referencia de repuesto (si aplica)?", TipoPregunta.TEXTO),
            )

            "forwarder" -> listOf(
                Pregunta("m3", "¿Producción en metros cúbicos (m³)?", TipoPregunta.NUMERO),
                Pregunta("pendiente", "¿Pendiente del terreno (en grados°)?", TipoPregunta.NUMERO),
                Pregunta("n_cargas", "¿N.º de cargas extraídas del lote?", TipoPregunta.NUMERO),
                Pregunta("peso_medio", "¿Peso medio por carga (toneladas)?", TipoPregunta.NUMERO),
                Pregunta(
                    "distancia",
                    "¿Distancia promedio por carga recorrida (metros)?",
                    TipoPregunta.NUMERO
                ),
                Pregunta("tiempo_programado", "¿Tiempo programado (h)?", TipoPregunta.NUMERO),
                Pregunta("alistamiento", "¿Tiempo en alistamiento (minutos)?", TipoPregunta.NUMERO),
                Pregunta("tanqueo", "¿Tiempo de tanqueo (minutos)?", TipoPregunta.NUMERO),
                Pregunta("alimentacion", "¿Tiempo de alimentación (minutos)?", TipoPregunta.NUMERO),
                Pregunta(
                    "paradas_mecanicas",
                    "¿Tiempo de paradas mecánicas (minutos)?",
                    TipoPregunta.NUMERO
                ),
                Pregunta(
                    "tEspecificado",
                    "Especificar parada mecanica",
                    TipoPregunta.SELECCION,
                    listOf("en reparacion", "esperando reparacion")
                ),
                Pregunta("repuesto", "¿Referencia de repuesto (si aplica)?", TipoPregunta.TEXTO),
            )

            else -> listOf(
                Pregunta("generica_1", "Pregunta común 1", TipoPregunta.TEXTO),
                Pregunta("generica_2", "Pregunta común 2", TipoPregunta.NUMERO)
            )
        }

        val preguntasNo = when (tipo.lowercase()) {
            "harvester", "forwarder" -> listOf(
                Pregunta("tiempo_programado", "¿Tiempo programado (h)?", TipoPregunta.NUMERO),
                Pregunta(
                    "paradas_mecanicas",
                    "¿Tiempo de paradas mecánicas (minutos)?",
                    TipoPregunta.NUMERO
                ),
                Pregunta("repuesto", "¿Referencia de repuesto (si aplica)?", TipoPregunta.TEXTO),
            )

            else -> listOf(
                Pregunta(
                    "no_equipo_generico",
                    "No hay preguntas para este equipo",
                    TipoPregunta.TEXTO
                )
            )
        }

        _uiState.value = _uiState.value.copy(
            tipoEquipo = tipo,
            preguntasOperativas = preguntasSi,
            preguntasNoOperativas = preguntasNo,
            respuestas = emptyMap(),
            paradasAdicionales = emptyList()
        )
    }

    fun setEstadoEquipo(enUso: Boolean) {
        _uiState.value = _uiState.value.copy(
            equipoEnUso = if (enUso) EstadoEquipo.EN_USO else EstadoEquipo.NO_OPERATIVO
        )
    }


    fun actualizarRespuesta(idPregunta: String, valor: String) {
        _uiState.value = _uiState.value.copy(
            respuestas = _uiState.value.respuestas.toMutableMap().apply {
                this[idPregunta] = valor
            }
        )
    }

    fun agregarParada() {
        _uiState.value = _uiState.value.copy(
            paradasAdicionales = _uiState.value.paradasAdicionales + Parada()
        )
    }

    fun deleteParada(index: Int) {
        val actualizadas = _uiState.value.paradasAdicionales.toMutableList()
        actualizadas.removeAt(index)
        _uiState.value = _uiState.value.copy(paradasAdicionales = actualizadas)
    }

    fun actualizarParada(index: Int, tiempo: String, motivo: String) {
        val actualizadas = _uiState.value.paradasAdicionales.toMutableList()
        actualizadas[index] = Parada(tiempo, motivo)
        _uiState.value = _uiState.value.copy(paradasAdicionales = actualizadas)
    }

    fun limpiarCamposNoSeleccionados() {
        val preguntasSi = _uiState.value.preguntasOperativas.map { it.id }.toSet()
        val preguntasNo = _uiState.value.preguntasNoOperativas.map { it.id }.toSet()
        val respuestasLimpias = _uiState.value.respuestas.toMutableMap()

        when (_uiState.value.equipoEnUso) {
            EstadoEquipo.EN_USO -> {
            }

            EstadoEquipo.NO_OPERATIVO -> {
                val camposALimpiar = preguntasSi - preguntasNo
                camposALimpiar.forEach { respuestasLimpias[it] = "0" }
            }

            else -> {
            }
        }

        _uiState.value = _uiState.value.copy(respuestas = respuestasLimpias)
    }

//    fun limpiarCamposNoSeleccionados() {
//        val campos = when (_uiState.value.equipoEnUso) {
//            EstadoEquipo.EN_USO -> _uiState.value.preguntasNoOperativas
//            EstadoEquipo.NO_OPERATIVO -> _uiState.value.preguntasOperativas
//            else -> emptyList()
//        }
//        val respuestasLimpias = _uiState.value.respuestas.toMutableMap()
//        campos.forEach { respuestasLimpias[it.id] = "0" }
//        _uiState.value = _uiState.value.copy(respuestas = respuestasLimpias)
//    }

    fun reset() {
        _uiState.value = DynamicFormUiState()
    }

    fun buildFormularioCompletoDynamic(): Any {


        val parcial =
            FormularioHolder.formulario ?: return error("No se ha encontrado un formulario previo")

        val tiempos = _uiState.value.paradasAdicionales.mapNotNull { it.tiempo.toDoubleOrNull() }
        val motivos = _uiState.value.paradasAdicionales.map { it.motivo.ifBlank { "Sin motivo" } }

        return when (parcial) {
            is FormularioCompletoFw -> {
                parcial.copy(
                    pendiente = _uiState.value.respuestas["pendiente"]?.toIntOrNull() ?: 0,
                    numeroCargas = _uiState.value.respuestas["n_cargas"]?.toIntOrNull() ?: 0,
                    viajesHora = _uiState.value.respuestas["fustes_hora"]?.toIntOrNull() ?: 0,
                    metrosCubicos = _uiState.value.respuestas["m3"]?.toDoubleOrNull() ?: 0.0,
                    pesoCargas = _uiState.value.respuestas["peso_medio"]?.toDoubleOrNull() ?: 0.0,
                    distancia = _uiState.value.respuestas["distancia"]?.toDoubleOrNull() ?: 0.0,
                    tProgramado = _uiState.value.respuestas["tiempo_programado"]?.toDoubleOrNull()
                        ?: 0.0,
                    tParadaMecanica = minutosAHoras(_uiState.value.respuestas["paradas_mecanicas"]),
                    especificacionParadaM = _uiState.value.respuestas["tEspecificado"] ?: "",
                    refRespuesto = _uiState.value.respuestas["repuesto"] ?: "",
                    tPorAlistamiento = minutosAHoras(_uiState.value.respuestas["alistamiento"]),
                    tanqueo = minutosAHoras(_uiState.value.respuestas["tanqueo"]),
                    alimentacion = minutosAHoras(_uiState.value.respuestas["alimentacion"]),
                    tUsoWinche = _uiState.value.respuestas["winche"]?.toDoubleOrNull() ?: 0.0,
                    novedades = _uiState.value.respuestas["novedad"] ?: "",
                    clima = _uiState.value.respuestas["clima"] ?: "",
                    hSueloSaturado = _uiState.value.respuestas["saturado"]?.toDoubleOrNull() ?: 0.0,
                    hParadas = 0.0,
                    totrasParadas = tiempos,
                    motivoOtrasParadas = motivos
                )
            }

            is FormularioCompletoHv -> {
                parcial.copy(
                    pendiente = _uiState.value.respuestas["pendiente"]?.toIntOrNull() ?: 0,
                    tArboles = _uiState.value.respuestas["fustes_total"]?.toIntOrNull() ?: 0,
                    metrosCubicos = _uiState.value.respuestas["m3"]?.toDoubleOrNull() ?: 0.0,
                    metrosCubicosH = _uiState.value.respuestas["m3_hora"]?.toDoubleOrNull() ?: 0.0,
                    fustesHora = _uiState.value.respuestas["fustes_hora"]?.toIntOrNull() ?: 0,
                    diametro = _uiState.value.respuestas["diametro"]?.toDoubleOrNull() ?: 0.0,
                    tProgramado = _uiState.value.respuestas["tiempo_programado"]?.toDoubleOrNull()
                        ?: 0.0,
                    tParadaMecanica = minutosAHoras(_uiState.value.respuestas["paradas_mecanicas"]),
                    especificacionParadaM = _uiState.value.respuestas["tEspecificado"] ?: "",
                    refRespuesto = _uiState.value.respuestas["repuesto"] ?: "",
                    tPorAlistamiento = minutosAHoras(_uiState.value.respuestas["alistamiento"]),
                    tanqueo = minutosAHoras(_uiState.value.respuestas["tanqueo"]),
                    alimentacion = minutosAHoras(_uiState.value.respuestas["alimentacion"]),
                    tUsoWinche = _uiState.value.respuestas["winche"]?.toDoubleOrNull() ?: 0.0,
                    novedades = _uiState.value.respuestas["novedad"] ?: "",
                    clima = _uiState.value.respuestas["suelo"] ?: "",
                    hParadas = 0.0,
                    totrasParadas = tiempos,
                    motivoOtrasParadas = motivos
                )
            }

            else -> parcial
        }
    }

    private fun minutosAHoras(valor: String?): Double {
        val minutos = valor?.toDoubleOrNull() ?: 0.0
        return minutos / 60.0
    }

}
