package com.example.movil_ctl.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asFlow
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.movil_ctl.worker.SyncWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SyncViewModel (application: Application) : AndroidViewModel(application) {
    private val workManager = WorkManager.getInstance(application)
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState

    sealed class SyncState {
        object Idle : SyncState()
        object InProgress : SyncState()
        data class Success(val message: String) : SyncState()
        data class Error(val error: String) : SyncState()
    }

    suspend fun triggerSync() {
        _syncState.value = SyncState.InProgress

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueue(syncRequest)

        // Observar el estado del trabajo
        workManager.getWorkInfoByIdLiveData(syncRequest.id)
            .asFlow()
            .collect { workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        _syncState.value = SyncState.Success("Sincronización completada")
                    }
                    WorkInfo.State.FAILED -> {
                        _syncState.value = SyncState.Error("Error en la sincronización")
                    }
                    else -> {}
                }
            }
    }
}