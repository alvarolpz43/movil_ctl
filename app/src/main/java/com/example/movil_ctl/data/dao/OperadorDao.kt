package com.example.movil_ctl.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.movil_ctl.data.entities.OperadorEntity
import kotlinx.coroutines.flow.Flow

interface OperadorDao {
    @Query("select * from operadores where equipos_id = :equiposId ")
    fun getOperadorByEquipo(equiposId: Int): Flow<List<OperadorEntity>>

    @Insert
    fun createOperadores(operadores: List<OperadorEntity>): Flow<List<OperadorEntity>>

    @Query("DELETE FROM operadores")
    suspend fun deleteAll()

}