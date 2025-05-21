package com.example.movil_ctl.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil_ctl.data.entities.EquipoEntity
import com.example.movil_ctl.repositories.CtlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EquipoViewModel @Inject constructor(
    private val repository: CtlRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    data class EquipoUiState(
        val equipos: List<EquipoEntity> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )


    private val contratistaId: String = savedStateHandle.get<String>("contratistaId")
        ?: throw IllegalStateException("Se requiere contratistaId")

    private val _uiState = MutableStateFlow(EquipoUiState())
    val uiState: StateFlow<EquipoUiState> = _uiState

    init {
        loadEquipos()
    }

    private fun loadEquipos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getEquiposByContratista(contratistaId).collect { lista ->
                    _uiState.value = EquipoUiState(
                        equipos = lista,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = EquipoUiState(
                    equipos = emptyList(),
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
