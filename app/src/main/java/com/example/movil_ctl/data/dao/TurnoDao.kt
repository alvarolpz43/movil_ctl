package com.example.movil_ctl.data.dao

import androidx.room.Insert
import androidx.room.Query
import com.example.movil_ctl.data.entities.TurnoEntity
import kotlinx.coroutines.flow.Flow

interface TurnoDao {
    @Query("select * from turnos where contratistas_id = :contratistaId")
    fun getAllTurnoByContratista(contratistaId: Int): Flow<List<TurnoEntity>>

    @Insert
    fun createTurnos(turnos: List<TurnoEntity>): Flow<List<TurnoEntity>>
}