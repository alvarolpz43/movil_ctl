package com.example.movil_ctl.data.dao

import androidx.room.Insert
import androidx.room.Query
import com.example.movil_ctl.data.entities.NucleosEntity
import kotlinx.coroutines.flow.Flow

interface NucleoDao {

    @Query("select * from nucleos")
    fun getAllNucleos(): Flow<List<NucleosEntity>>

    @Insert
    fun createNucleos(nucleos: List<NucleosEntity>): Flow<List<NucleosEntity>>
}