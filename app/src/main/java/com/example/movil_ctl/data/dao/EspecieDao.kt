package com.example.movil_ctl.data.dao

import androidx.room.Insert
import androidx.room.Query
import com.example.movil_ctl.data.entities.EspecieEntity
import kotlinx.coroutines.flow.Flow

interface EspecieDao {

    @Query("select * from especies")
    fun getAllEspecies(): Flow<List<EspecieEntity>>

    @Insert
    fun createEspecies(especies: List<EspecieEntity>): Flow<List<EspecieEntity>>
}