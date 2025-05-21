package com.example.movil_ctl.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.movil_ctl.data.constants.FormularioCompletoFw
import com.example.movil_ctl.data.constants.FormularioCompletoHv
import com.example.movil_ctl.data.constants.FormularioHolder
import com.example.movil_ctl.data.dataui.EndScreenUiState
import com.example.movil_ctl.data.dataui.Pregunta
import com.example.movil_ctl.data.dataui.TipoPregunta
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class EndScreenFormViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(EndScreenUiState())
    val uiState: StateFlow<EndScreenUiState> = _uiState


    fun setTipoEquipo(tipo: String) {
        val preguntas = when (tipo.lowercase()) {
            "harvester" -> listOf(
                Pregunta("winche", "¿Tiempo uso del winche (h)?", TipoPregunta.NUMERO),
                Pregunta("novedad", "Novedades", TipoPregunta.TEXTO),
                Pregunta("suelo", "Suelo", TipoPregunta.SELECCION, listOf("Humedo", "Seco"))
            )

            "forwarder" -> listOf(
                Pregunta("saturado", "¿Horas Suelo Saturado?", TipoPregunta.NUMERO),
                Pregunta("winche", "¿Tiempo uso del winche (h)?", TipoPregunta.NUMERO),
                Pregunta("novedad", "Novedades", TipoPregunta.TEXTO),
                Pregunta("suelo", "Suelo", TipoPregunta.SELECCION, listOf("Humedo", "Seco"))
            )

            else -> listOf(
                Pregunta("generica_1", "Pregunta común 1", TipoPregunta.TEXTO),
                Pregunta("generica_2", "Pregunta común 2", TipoPregunta.NUMERO)
            )
        }


        _uiState.value = _uiState.value.copy(
            tipoEquipo = tipo, preguntas = preguntas, respuestas = emptyMap()
        )

    }

    fun actualizarFormularioConRespuestasFinales() {
        val respuestas = _uiState.value.respuestas
        val formulario = FormularioHolder.formulario

        when (formulario) {
            is FormularioCompletoFw -> {
                FormularioHolder.formulario = formulario.copy(
                    tUsoWinche = respuestas["winche"]?.toDoubleOrNull() ?: formulario.tUsoWinche,
                    novedades = respuestas["novedad"] ?: formulario.novedades,
                    clima = respuestas["suelo"] ?: formulario.clima,
                    hSueloSaturado = respuestas["saturado"]?.toDoubleOrNull()
                        ?: formulario.hSueloSaturado
                )
            }

            is FormularioCompletoHv -> {
                FormularioHolder.formulario = formulario.copy(
                    tUsoWinche = respuestas["winche"]?.toDoubleOrNull() ?: formulario.tUsoWinche,
                    novedades = respuestas["novedad"] ?: formulario.novedades,
                    clima = respuestas["suelo"] ?: formulario.clima
                )
            }

            else -> {
                // No hacer nada si no es un formulario válido
            }
        }
    }

    private fun formatAny(value: Any?): String {
        return when (value) {
            null -> ""
            is String -> if (value.isBlank()) "" else value
            is Double -> if (value == 0.0) "" else String.format("%.2f", value)
            is Int -> if (value == 0) "" else value.toString()
            is Float -> if (value == 0f) "" else String.format("%.2f", value)
            else -> value.toString()
        }
    }


    fun generateTxt(context: Context): File {
        actualizarFormularioConRespuestasFinales()
        val formulario =
            FormularioHolder.formulario ?: error("No se ha encontrado un formulario previo")


        val (contenido, tipo) = when (formulario) {
            is FormularioCompletoFw -> {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val fechaFormateada = formatter.format(formulario.fecha)

                val contenidoFw = buildString {
                    append(
                        listOf(
                            formulario.serieEquipo.uppercase(),
                            formulario.nombreOperador.uppercase(),
                            formulario.cedulaOperador,
                            formulario.zona.uppercase(),
                            formulario.nombreContratista.uppercase(),
                            fechaFormateada,
                            formulario.finca.uppercase(),
                            formulario.especie.uppercase(),
                            formulario.lote.uppercase(),
                            formulario.turno.uppercase(),
                            formatAny(formulario.pendiente),
                            formatAny(formulario.numeroCargas),
                            formatAny(formulario.viajesHora),
                            formatAny(formulario.metrosCubicos),
                            formatAny(formulario.pesoCargas),
                            formatAny(formulario.distancia),
                            formatAny(formulario.tProgramado),
                            formatAny(formulario.tParadaMecanica),
                            formulario.especificacionParadaM.uppercase(),
                            formulario.refRespuesto.uppercase(),
                            formatAny(formulario.tPorAlistamiento),
                            formatAny(formulario.tanqueo),
                            formatAny(formulario.alimentacion),
                            formatAny(formulario.tUsoWinche),
                            formulario.novedades.uppercase(),
                            formulario.clima.uppercase(),
                            formatAny(formulario.hSueloSaturado),
                            formatAny(formulario.hParadas)
                        ).joinToString(";")
                    )
                    formulario.totrasParadas.forEach { append(";$it") }
                    formulario.motivoOtrasParadas.forEach { append(";$it") }
                }
                contenidoFw to "forwarder"
            }

            is FormularioCompletoHv -> {
                val contenidoHv = buildString {
                    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    val fechaFormateada = formatter.format(formulario.fecha)
                    append(
                        listOf(
                            formulario.serieEquipo.uppercase(),
                            formulario.nombreOperador.uppercase(),
                            formulario.cedulaOperador,
                            formulario.zona.uppercase(),
                            formulario.nombreContratista.uppercase(),
                            fechaFormateada,
                            formulario.finca.uppercase(),
                            formulario.especie.uppercase(),
                            formulario.lote.uppercase(),
                            formulario.turno.uppercase(),
                            formatAny(formulario.pendiente),
                            formatAny(formulario.tArboles),
                            formatAny(formulario.metrosCubicos),
                            formatAny(formulario.metrosCubicosH),
                            formatAny(formulario.fustesHora),
                            formatAny(formulario.diametro),
                            formatAny(formulario.tProgramado),
                            formatAny(formulario.tParadaMecanica),
                            formulario.especificacionParadaM.uppercase(),
                            formulario.refRespuesto.uppercase(),
                            formatAny(formulario.tPorAlistamiento),
                            formatAny(formulario.tanqueo),
                            formatAny(formulario.alimentacion),
                            formatAny(formulario.tUsoWinche),
                            formulario.novedades.uppercase(),
                            formulario.clima.uppercase(),
                            formatAny(formulario.hParadas)
                        ).joinToString(";")
                    )
                    formulario.totrasParadas.forEach { append(";$it") }
                    formulario.motivoOtrasParadas.forEach { append(";$it") }
                }
                contenidoHv to "harvester"
            }

            else -> "0" to "desconocido"
        }

        val fileName = "formulario_${tipo}_${System.currentTimeMillis()}.txt"
        val file = File(context.filesDir, fileName)
        file.writeText(contenido)
        return file
    }


    fun actualizarRespuesta(idPregunta: String, valor: String) {
        _uiState.value =
            _uiState.value.copy(respuestas = _uiState.value.respuestas.toMutableMap().apply {
                this[idPregunta] = valor
            })
    }


    fun getFileUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context, "${context.packageName}.provider", file
        )
    }

    fun buildShareIntent(uri: Uri, context: Context): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            setPackage("com.whatsapp")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }


}