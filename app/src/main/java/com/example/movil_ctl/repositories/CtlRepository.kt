package com.example.movil_ctl.repositories

import com.example.movil_ctl.data.AppDatabase
import com.example.movil_ctl.data.entities.ContratistaEntity
import com.example.movil_ctl.data.entities.EquipoEntity
import com.example.movil_ctl.data.entities.EspecieEntity
import com.example.movil_ctl.data.entities.FincasEntity
import com.example.movil_ctl.data.entities.NucleosEntity
import com.example.movil_ctl.data.entities.OperadorEntity
import com.example.movil_ctl.data.entities.RegistroEntity
import com.example.movil_ctl.data.entities.TurnoEntity
import com.example.movil_ctl.data.entities.ZonasEntity

class CtlRepository(private val db: AppDatabase) {
    // Operaciones con equipos
    suspend fun saveEquipos(equipos: List<EquipoEntity>) {
        db.equipoDao().insertAll(equipos)
    }

    suspend fun deleteEquipos() {
        db.equipoDao().deleteAll()
    }

    // Operaciones con operadores
    suspend fun saveOperadores(operadores: List<OperadorEntity>) {
        db.operadorDao().createOperadores(operadores)
    }

    suspend fun deleteOperadores() {
        db.operadorDao().deleteAll()
    }


    suspend fun saveContratistas(contratistas: List<ContratistaEntity>) {
        db.contratistaDao().insertAll(contratistas)
    }

    suspend fun deleteContratistas() {
        db.contratistaDao().deleteAll()
    }

    suspend fun saveEspecies(especies: List<EspecieEntity>) {
        db.especieDao().createEspecies(especies)
    }

    suspend fun deleteEspecies() {
        db.especieDao().deleteAll()
    }

    suspend fun saveTurnos(turnos: List<TurnoEntity>) {
        db.turnoDao().createTurnos(turnos)
    }

    suspend fun deleteTurnos() {
        db.turnoDao().deleteAll()
    }

    suspend fun saveZonas(zonas: List<ZonasEntity>) {
        db.zonaDao().insertZonas(zonas)
    }

    suspend fun deleteZonas() {
        db.zonaDao().deleteAll()
    }

    suspend fun getAllZonas(): List<ZonasEntity>{
        return db.zonaDao().getAllZonas()
    }

    suspend fun saveNucleos(nucleos: List<NucleosEntity>) {
        db.nucleoDao().createNucleos(nucleos)
    }

    suspend fun deleteNucleos() {
        db.nucleoDao().deleteAll()
    }

    suspend fun getAllNucleos(): List<NucleosEntity>{
        return db.nucleoDao().getAllNucleos()
    }

    suspend fun saveFincas(fincas: List<FincasEntity>) {
        db.fincaDao().createFincas(fincas)
    }

    suspend fun deleteFincas() {
        db.fincaDao().deleteAll()
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