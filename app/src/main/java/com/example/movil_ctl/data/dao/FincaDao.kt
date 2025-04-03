package com.example.movil_ctl.data.dao

import androidx.room.Insert
import androidx.room.Query
import com.example.movil_ctl.data.entities.FincasEntity
import kotlinx.coroutines.flow.Flow

interface FincaDao {
    @Query("select * from zonas")
    fun getAllFincas(): Flow<List<FincasEntity>>

    @Insert
    fun createFincas(fincas: List<FincasEntity>): Flow<List<FincasEntity>>

}