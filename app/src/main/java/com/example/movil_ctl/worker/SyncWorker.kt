package com.example.movil_ctl.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.movil_ctl.core.ApiClient
import com.example.movil_ctl.data.AppDatabase
import com.example.movil_ctl.data.entities.ContratistaEntity
import com.example.movil_ctl.data.entities.EquipoEntity
import com.example.movil_ctl.data.entities.EspecieEntity
import com.example.movil_ctl.data.entities.FincasEntity
import com.example.movil_ctl.data.entities.NucleosEntity
import com.example.movil_ctl.data.entities.OperadorEntity
import com.example.movil_ctl.data.entities.TurnoEntity
import com.example.movil_ctl.data.entities.ZonasEntity
import com.example.movil_ctl.repositories.CtlRepository


class SyncWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val api = ApiClient.instance
    private val repository = CtlRepository(AppDatabase.getDatabase(context))

    override suspend fun doWork(): Result {
        return try {
            // 1. Sincronizar entidades sin dependencias
            syncContratistas()
            syncEspecies()

            // 2. Sincronizar jerarquía crítica: Zonas → Nucleos → Fincas
            syncZonas()
            syncNucleos()
            syncFincas()

            // 3. Sincronizar dependencias restantes
            syncEquipos()
            syncOperadores()
            syncTurnos()

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun syncContratistas() {
        try {
            val response = api.getContratistas() // Retorna Response<ContratistaResponse>
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) { // Verifica body.success
                    repository.deleteContratistas()

                    // body.data ya es List<ContratistaEstructure>
                    val contratistasEntities = body.data.map { contratistaStructure ->
                        ContratistaEntity(
                            id = contratistaStructure.id,
                            nombre = contratistaStructure.nombre,
                            estado = contratistaStructure.estado
                        )
                    }

                    if (contratistasEntities.isNotEmpty()) {
                        repository.saveContratistas(contratistasEntities)
                        Log.d(
                            "SyncWorker",
                            "Sincronizados ${contratistasEntities.size} contratistas"
                        )
                    } else {
                        Log.w("SyncWorker", "La lista de contratistas está vacía")
                    }
                } else {
                    throw Exception("El cuerpo de la respuesta es nulo o success=false")
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

                    repository.deleteEquipos()

                    val equiposEntities = body.data.map { equipoResponse ->

                        EquipoEntity(
                            id = equipoResponse.id,
                            nombreEquipo = equipoResponse.nombreEquipo,
                            serieEquipo = equipoResponse.serieEquipo,
                            tipoEquipo = equipoResponse.tipoEquipo,
                            contratistasId = equipoResponse.contratista
                        )

                    }

                    if (equiposEntities.isNotEmpty()) {
                        repository.saveEquipos(equiposEntities)
                        Log.d("SyncWorker", "Sincronizados ${equiposEntities.size} equipos")
                    } else {
                        Log.w("SyncWorker", "La lista de equipos está vacía")
                    }
                } else {
                    Log.e("SyncWorker", "Respuesta de equipos no exitosa o cuerpo nulo")
                    throw Exception("Respuesta de equipos no exitosa o cuerpo nulo")
                }
            } else {
                val errorMsg = "Error HTTP al obtener equipos: ${response.code()} - ${
                    response.errorBody()?.string()
                }"
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
                if (body != null) {

                    repository.deleteOperadores()

                    val operadoresEntities = body.data.map { operadorResponse ->
                        OperadorEntity(
                            id = operadorResponse.id,
                            nombreOperador = operadorResponse.nombreOperador,
                            cedulaOperador = operadorResponse.cedulaOperador,
                            equiposId = operadorResponse.equipo.id
                        )

                    }

                    if (operadoresEntities.isNotEmpty()) {
                        repository.saveOperadores(operadoresEntities)
                        Log.d("SyncWorker", "Sincronizados ${operadoresEntities.size} operadores")
                    } else {
                        Log.w("SyncWorker", "La lista de operadores está vacía")
                    }
                } else {
                    Log.e("SyncWorker", "Respuesta de operadores no exitosa o cuerpo nulo")
                    throw Exception("Respuesta de operadores no exitosa o cuerpo nulo")
                }
            } else {
                val errorMsg = "Error HTTP al obtener operadores: ${response.code()} - ${
                    response.errorBody()?.string()
                }"
                Log.e("SyncWorker", errorMsg)
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error al sincronizar operadores", e)
            throw e
        }
    }

    private suspend fun syncEspecies() {
        try {
            val response = api.getEspecies()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {

                    repository.deleteEspecies()

                    val especiesEntities = body.data.map { especiesResponse ->
                        EspecieEntity(
                            id = especiesResponse.id,
                            nombre = especiesResponse.nombre
                        )

                    }

                    if (especiesEntities.isNotEmpty()) {
                        repository.saveEspecies(especiesEntities)
                        Log.d("SyncWorker", "Sincronizados ${especiesEntities.size} especies")
                    } else {
                        Log.w("SyncWorker", "La lista de especies está vacía")
                    }
                } else {
                    Log.e("SyncWorker", "Respuesta de especies no exitosa o cuerpo nulo")
                    throw Exception("Respuesta de especies no exitosa o cuerpo nulo")
                }
            } else {
                val errorMsg = "Error HTTP al obtener especies: ${response.code()} - ${
                    response.errorBody()?.string()
                }"
                Log.e("SyncWorker", errorMsg)
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error al sincronizar especies", e)
            throw e
        }
    }

    private suspend fun syncTurnos() {
        try {
            val response = api.getTurnos()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {

                    repository.deleteTurnos()

                    val turnosEntities = body.data.map { turnosResponse ->
                        TurnoEntity(
                            id = turnosResponse._id ?: "",
                            nombre = turnosResponse.nombreTurno ?: "",
                            horaInicio = turnosResponse.horaInicio ?: "",
                            horaFin = turnosResponse.horaFin ?: "",
                            contratistasId = turnosResponse.contratistaId!!.id

                        )

                    }

                    if (turnosEntities.isNotEmpty()) {
                        repository.saveTurnos(turnosEntities)
                        Log.d("SyncWorker", "Sincronizados ${turnosEntities.size} turnos")
                    } else {
                        Log.w("SyncWorker", "La lista de turnos está vacía")
                    }
                } else {
                    Log.e("SyncWorker", "Respuesta de turnos no exitosa o cuerpo nulo")
                    throw Exception("Respuesta de turnos no exitosa o cuerpo nulo")
                }
            } else {
                val errorMsg = "Error HTTP al obtener turnos: ${response.code()} - ${
                    response.errorBody()?.string()
                }"
                Log.e("SyncWorker", errorMsg)
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error al sincronizar turnos", e)
            throw e
        }
    }

    private suspend fun syncZonas() {
        try {
            val response = api.getZonas()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {

                    repository.deleteZonas()

                    val zonasEntities = body.data.map { zonasResponse ->
                        ZonasEntity(
                            id = zonasResponse.id,
                            nombre = zonasResponse.nombreZona,
                            codZona = zonasResponse.codeZona
                        )

                    }

                    if (zonasEntities.isNotEmpty()) {
                        repository.saveZonas(zonasEntities)
                        Log.d("SyncWorker", "Sincronizados ${zonasEntities.size} zonas")
                    } else {
                        Log.w("SyncWorker", "La lista de zonas está vacía")
                    }
                } else {
                    Log.e("SyncWorker", "Respuesta de zonas no exitosa o cuerpo nulo")
                    throw Exception("Respuesta de zonas no exitosa o cuerpo nulo")
                }
            } else {
                val errorMsg = "Error HTTP al obtener zonas: ${response.code()} - ${
                    response.errorBody()?.string()
                }"
                Log.e("SyncWorker", errorMsg)
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error al sincronizar zonas", e)
            throw e
        }
    }

    private suspend fun syncNucleos() {
        try {
            val response = api.getNucleos()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {

                    repository.deleteNucleos()


                    val nucleosEntities = body.data.map { nucleosResponse ->
                        NucleosEntity(
                            id = nucleosResponse.id,
                            nombre = nucleosResponse.nombreNucleo,
                            codNucleo = nucleosResponse.codeNucleo,
                            zonaId = nucleosResponse.zonaId
                        )

                    }


                    val zonasIds = repository.getAllZonas().map { it.id }


                    val nucleosInvalidos = nucleosEntities.filter { it.zonaId !in zonasIds }
                    if (nucleosInvalidos.isNotEmpty()) {
                        Log.e(
                            "SyncWorker",
                            "ZonaIds no encontrados: ${nucleosInvalidos.map { it.zonaId }}"
                        )
                        throw Exception("ZonaIds inválidos en núcleos")
                    }

                    if (nucleosEntities.isNotEmpty()) {
                        repository.saveNucleos(nucleosEntities)
                        Log.d("SyncWorker", "Sincronizados ${nucleosEntities.size} nucleos")
                    } else {
                        Log.w("SyncWorker", "La lista de nucleos está vacía")
                    }
                } else {
                    Log.e("SyncWorker", "Respuesta de nucleos no exitosa o cuerpo nulo")
                    throw Exception("Respuesta de nucleos no exitosa o cuerpo nulo")
                }
            } else {
                val errorMsg = "Error HTTP al obtener nucleos: ${response.code()} - ${
                    response.errorBody()?.string()
                }"
                Log.e("SyncWorker", errorMsg)
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error al sincronizar nucleos", e)
            throw e
        }
    }

    private suspend fun syncFincas() {
        try {
            val response = api.getFincas()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {

                    repository.deleteFincas()

                    val fincasEntities = body.data.map { fincasResponse ->
                        FincasEntity(
                            id = fincasResponse.id,
                            nombre = fincasResponse.nombreFinca,
                            codFinca = fincasResponse.codeFinca,
                            nucleoId = fincasResponse.nucleoId
                        )

                    }


                    val nucleosIds = repository.getAllNucleos().map { it.id }
                    Log.d("SyncWorker", "IDs de núcleos en BD: $nucleosIds")

                    val fincasInvalidas = fincasEntities.filter { it.nucleoId !in nucleosIds }
                    if (fincasInvalidas.isNotEmpty()) {
                        Log.e(
                            "SyncWorker",
                            "NucleoIds no encontrados: ${fincasInvalidas.map { it.nucleoId }}"
                        )
                        throw Exception("NucleoIds inválidos en fincas")
                    }

                    if (fincasEntities.isNotEmpty()) {
                        repository.saveFincas(fincasEntities)
                        Log.d("SyncWorker", "Sincronizados ${fincasEntities.size} fincas")
                    } else {
                        Log.w("SyncWorker", "La lista de fincas está vacía")
                    }
                } else {
                    Log.e("SyncWorker", "Respuesta de fincas no exitosa o cuerpo nulo")
                    throw Exception("Respuesta de fincas no exitosa o cuerpo nulo")
                }
            } else {
                val errorMsg = "Error HTTP al obtener fincas: ${response.code()} - ${
                    response.errorBody()?.string()
                }"
                Log.e("SyncWorker", errorMsg)
                throw Exception(errorMsg)
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error al sincronizar fincas", e)
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