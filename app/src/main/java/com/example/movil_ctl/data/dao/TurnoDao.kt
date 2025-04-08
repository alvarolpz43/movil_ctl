package com.example.movil_ctl.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movil_ctl.data.entities.TurnoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TurnoDao {
    @Query("select * from turnos where contratistas_id = :contratistaId")
    fun getAllTurnoByContratista(contratistaId: Int): Flow<List<TurnoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createTurnos(turnos: List<TurnoEntity>)

    @Query("DELETE FROM turnos")
    fun deleteAll()
}
