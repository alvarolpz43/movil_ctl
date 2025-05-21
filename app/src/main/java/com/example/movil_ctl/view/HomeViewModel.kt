// HomeViewModel.kt

package com.example.movil_ctl
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movil_ctl.data.entities.ContratistaEntity
import com.example.movil_ctl.repositories.CtlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor (private val repository: CtlRepository) : ViewModel() {

    data class HomeUiState(
        val isLoading: Boolean = false,
        val contratistas: List<ContratistaEntity> = emptyList(),
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadContratistas()
    }

     fun loadContratistas() {
        viewModelScope.launch {
            repository.getContratistas()
                .catch { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
                .collect { contratistas ->
                    _uiState.update {
                        it.copy(
                            contratistas = contratistas,
                            isLoading = false
                        )
                    }
                }
        }
    }
}