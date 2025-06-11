package com.example.movil_ctl.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil_ctl.data.constants.FormularioCompletoFw
import com.example.movil_ctl.data.constants.FormularioCompletoHv
import com.example.movil_ctl.data.entities.EspecieEntity
import com.example.movil_ctl.data.entities.FincasEntity
import com.example.movil_ctl.data.entities.NucleosEntity
import com.example.movil_ctl.data.entities.OperadorEntity
import com.example.movil_ctl.data.entities.TurnoEntity
import com.example.movil_ctl.data.entities.ZonasEntity
import com.example.movil_ctl.repositories.CtlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FirstFormSViewModel @Inject constructor(
    private val repository: CtlRepository
) : ViewModel() {


    data class FirstFormUiState(
        val nombreContratista: String = "",
        val serieEquipo: String = "",
        val equipoNombre: String = "",  // <- Nueva propiedad
        val contratistaNombre: String = "",  // <- Nueva propiedad
        val zonas: List<ZonasEntity> = emptyList(),
        val nucleos: List<NucleosEntity> = emptyList(),
        val fincas: List<FincasEntity> = emptyList(),
        val especies: List<EspecieEntity> = emptyList(),
        val turnos: List<TurnoEntity> = emptyList(),
        val operadores: List<OperadorEntity> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _selectedZonaId = MutableStateFlow<String?>(null)
    private val _selectedNucleoId = MutableStateFlow<String?>(null)

    val selectedZonaId: StateFlow<String?> = _selectedZonaId
    val selectedNucleoId: StateFlow<String?> = _selectedNucleoId

    private val _nucleos = MutableStateFlow<List<NucleosEntity>>(emptyList())
    val nucleos: StateFlow<List<NucleosEntity>> = _nucleos

    private val _fincas = MutableStateFlow<List<FincasEntity>>(emptyList())
    val fincas: StateFlow<List<FincasEntity>> = _fincas

    private val _lote = MutableStateFlow<String>("")
    val lote: StateFlow<String> = _lote

    private val _uiState = MutableStateFlow(FirstFormUiState(isLoading = true))
    val uiState: StateFlow<FirstFormUiState> = _uiState

    fun onZonaSelected(zonaId: String) {
        _selectedZonaId.value = zonaId
        viewModelScope.launch {
            repository.getNucleosByZona(zonaId).collect { lista ->
                _nucleos.value = lista
            }
        }
    }

    fun setLote(valor: String) {
        _lote.value = valor
    }

    fun onNucleoSelected(nucleoId: String) {
        _selectedNucleoId.value = nucleoId
        viewModelScope.launch {
            repository.getFincasByNucleos(nucleoId).collect { lista ->
                _fincas.value = lista
            }
        }
    }

    fun nextSceen(contratistaId: String, equipoId: String) {
        // navegación manejada desde el Composable
    }


    fun buildFormularioCompleto(
        tipoEquipo: String,
        selectedFecha: Date,
        selectedZonaName: String,
        selectedFincaName: String,
        selectedCodeFinca: String,
        selectedEspecieName: String,
        selectedTurnoName: String,
        selectedOperadorName: String
    ): Any {
        // extraer cédula del operador
        val operador = _uiState.value.operadores.find { it.nombreOperador == selectedOperadorName }
        val cedula = operador?.cedulaOperador ?: ""

        return if (tipoEquipo.equals("forwarder", true)) {
            FormularioCompletoFw(
                serieEquipo = _uiState.value.equipoNombre,
                nombreOperador = selectedOperadorName,
                cedulaOperador = cedula,
                zona = selectedZonaName,
                nombreContratista = _uiState.value.nombreContratista,
                fecha = selectedFecha,
                finca = selectedCodeFinca,
                especie = selectedEspecieName,
                lote = lote.value,
                turno = selectedTurnoName,
                pendiente = 0,
                numeroCargas = 0,
                viajesHora = 0,
                metrosCubicos = 0.0,
                pesoCargas = 0.0,
                distancia = 0.0,
                tProgramado = 0.0,
                tParadaMecanica = 0.0,
                especificacionParadaM = "",
                refRespuesto = "",
                tPorAlistamiento = 0.0,
                tanqueo = 0.0,
                alimentacion = 0.0,
                tUsoWinche = 0.0,
                novedades = "",
                clima = "",
                hSueloSaturado = 0.0,
                hParadas = 0.0,
                totrasParadas = emptyList(),
                motivoOtrasParadas = emptyList()
            )
        } else {
            FormularioCompletoHv(
                serieEquipo = _uiState.value.equipoNombre,
                nombreOperador = selectedOperadorName,
                cedulaOperador = cedula,
                zona = selectedZonaName,
                nombreContratista = _uiState.value.nombreContratista,
                fecha = Date(),
                finca = selectedCodeFinca,
                especie = selectedEspecieName,
                lote = lote.value,
                turno = selectedTurnoName,
                pendiente = 0,
                tArboles = 0,
                metrosCubicos = 0.0,
                metrosCubicosH = 0.0,
                fustesHora = 0,
                diametro = 0.0,
                tProgramado = 0.0,
                tParadaMecanica = 0.0,
                especificacionParadaM = "",
                refRespuesto = "",
                tPorAlistamiento = 0.0,
                tanqueo = 0.0,
                alimentacion = 0.0,
                tUsoWinche = 0.0,
                novedades = "",
                clima = "",
                hParadas = 0.0,
                totrasParadas = emptyList(),
                motivoOtrasParadas = emptyList()
            )
        }
    }


    fun loadFirstFormScreen(contratistaId: String, equipoId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val zonas = repository.getAllZonas()
                val especies = repository.getAllEspecies().first()
                val turnos = repository.getTurnosByContratista(contratistaId).first()
                val operadores = repository.getOperadoresByEquipo(equipoId).first()

                val contratista = repository.getContratistaById(contratistaId)
                val nombreContr = contratista?.nombre ?: ""

                val equiposList = repository.getEquiposByContratista(contratistaId).first()
                val equipoSel = equiposList.find { it.id == equipoId }
                val serieEq = equipoSel?.serieEquipo ?: ""
                val nombreEq = equipoSel?.nombreEquipo ?: ""  // <- Nuevo valor

                val nucleosList =
                    selectedZonaId.value?.let { repository.getNucleosByZona(it).first() }
                        ?: emptyList()
                val fincasList =
                    selectedNucleoId.value?.let { repository.getFincasByNucleos(it).first() }
                        ?: emptyList()

                _uiState.value = FirstFormUiState(
                    nombreContratista = nombreContr,
                    contratistaNombre = nombreContr,
                    serieEquipo = serieEq,
                    equipoNombre = nombreEq,
                    zonas = zonas,
                    nucleos = nucleosList,
                    fincas = fincasList,
                    especies = especies,
                    turnos = turnos,
                    operadores = operadores,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}
