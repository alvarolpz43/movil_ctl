package com.example.movil_ctl.data.dao

import androidx.room.Insert
import androidx.room.Query
import com.example.movil_ctl.data.entities.ZonasEntity
import kotlinx.coroutines.flow.Flow

interface ZonaDao {

    @Query("select * from zonas")
    fun getAllZonas(): Flow<List<ZonasEntity>>

    @Insert
    fun createZonas(zonas: List<ZonasEntity>): Flow<List<ZonasEntity>>
}