package com.example.movil_ctl.repositories

import com.example.movil_ctl.data.AppDatabase
import com.example.movil_ctl.data.entities.ContratistaEntity
import com.example.movil_ctl.data.entities.EquipoEntity
import com.example.movil_ctl.data.entities.OperadorEntity
import com.example.movil_ctl.data.entities.RegistroEntity

class CtlRepository(private val db: AppDatabase) {
    // Operaciones con equipos
    suspend fun saveEquipos(equipos: List<EquipoEntity>) {
        db.equipoDao().insertAll(equipos)
    }

    // Operaciones con operadores
    suspend fun saveOperadores(operadores: List<OperadorEntity>) {
        db.operadorDao().createOperadores(operadores)
    }

    suspend fun saveContratistas(contratistas: List<ContratistaEntity>) {
        db.contratistaDao().insertAll(contratistas)
    }

    // Sincronización de registros
//    suspend fun getUnsyncedRegistros(): List<RegistroEntity> {
//        return db.registroDao().getUnsynced()
//    }

//    suspend fun markAsSynced(ids: List<String>) {
//        db.registroDao().markAsSynced(ids)
//    }

//    // Manejo de fechas de sincronización
//    suspend fun getLastSyncDate(): Date? {
//        return db.syncStatusDao().getLastSync()
//    }

//    suspend fun updateLastSyncDate(date: Date) {
//        db.syncStatusDao().updateLastSync(date)
//    }

}