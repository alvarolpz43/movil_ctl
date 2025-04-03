package com.example.movil_ctl.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.movil_ctl.core.ApiClient
import com.example.movil_ctl.data.AppDatabase
import com.example.movil_ctl.data.entities.ContratistaEntity
import com.example.movil_ctl.data.entities.EquipoEntity
import com.example.movil_ctl.repositories.CtlRepository


class SyncWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val api = ApiClient.instance
    private val repository = CtlRepository(AppDatabase.getDatabase(context))

    override suspend fun doWork(): Result {
        return try {

            Result.success()
        }catch (e: Exception){
            Result.retry()
        }
    }
    private suspend fun syncContratistas() {
        try {
            val response = api.getContratistas()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    // Como ahora es List<ContratistaResponse>, necesitamos flatMap
                    val contratistasEntities = body.flatMap { contratistaResponse ->
                        contratistaResponse.data.map { contratistaStructure ->
                            ContratistaEntity(
                                id = contratistaStructure.id,
                                nombre = contratistaStructure.nombre,
                                estado = contratistaStructure.estado
                            )
                        }
                    }

                    if (contratistasEntities.isNotEmpty()) {
                        repository.saveContratistas(contratistasEntities)
                        Log.d("SyncWorker", "Sincronizados ${contratistasEntities.size} contratistas")
                    } else {
                        Log.w("SyncWorker", "La lista de contratistas está vacía")
                    }
                } else {
                    throw Exception("El cuerpo de la respuesta es nulo")
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Sin mensaje de error"
                throw Exception("Error HTTP ${response.code()}: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error en syncContratistas", e)
            throw e
        }
    }

    private suspend fun syncEquipos() {
        try {
            val response = api.getEquipos()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {

                    val equiposEntities = body.flatMap { equipoResponse ->
                        equipoResponse.data.map { equipoStructure ->
                            EquipoEntity(
                                id = equipoStructure.id,
                                nombreEquipo = equipoStructure.nombreEquipo,
                                serieEquipo = equipoStructure.serieEquipo,
                                contratistasId = equipoStructure.contratista.id
                            )
                        }
                    }

                    if (equiposEntities.isNotEmpty()) {
                        repository.saveEquipos(equiposEntities)
                        Log.d("SyncWorker", "Sincronizados ${equiposEntities.size} contratistas")
                    } else {
                        Log.w("SyncWorker", "La lista de contratistas está vacía")
                    }
                } else {
                    Log.e("SyncWorker", "Respuesta de equipos no exitosa o cuerpo nulo")
                    throw Exception("Respuesta de equipos no exitosa o cuerpo nulo")
                }
            } else {
                val errorMsg = "Error HTTP al obtener equipos: ${response.code()} - ${response.errorBody()?.string()}"
                Log.e("SyncWorker", errorMsg)
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error al sincronizar equipos", e)
            throw e
        }
    }

    private suspend fun syncOperadores() {
        try {
            val response = api.getOperadores()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    body.data?.let { operadores ->
                        repository.saveOperadores(operadores.map { it.toLocalOperador() })
                    }
                } else {
                    Log.e("SyncWorker", "Respuesta de operadores no exitosa o cuerpo nulo")
                    throw Exception("Respuesta de operadores no exitosa o cuerpo nulo")
                }
            } else {
                val errorMsg = "Error HTTP al obtener operadores: ${response.code()} - ${response.errorBody()?.string()}"
                Log.e("SyncWorker", errorMsg)
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error al sincronizar operadores", e)
            throw e
        }
    }


//    private suspend fun syncIncrementalChanges() {
//        val lastSync = repository.getLastSyncDate()?.toFormattedString()
//
//        // Sincronización incremental de operadores
//        val operadoresIncrementalResponse = api.getOperadoresSince(lastSync)
//        if (operadoresIncrementalResponse.isSuccessful && operadoresIncrementalResponse.body()?.success == true) {
//            operadoresIncrementalResponse.body()?.data?.let { operadores ->
//                repository.saveOperadores(operadores.map { it.toEntity() })
//            }
//        }
//
//        // Actualizar fecha de última sincronización
//        repository.updateLastSyncDate(Date())
//    }
}