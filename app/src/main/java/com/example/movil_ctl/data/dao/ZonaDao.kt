package com.example.movil_ctl.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movil_ctl.data.entities.ZonasEntity

@Dao
interface ZonaDao {

    @Query("select * from zonas")
    suspend fun getAllZonas(): List<ZonasEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZonas(zonas: List<ZonasEntity>)

    @Query("DELETE FROM zonas")
    suspend fun deleteAll()

}