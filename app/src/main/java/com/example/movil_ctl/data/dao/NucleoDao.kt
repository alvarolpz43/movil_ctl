package com.example.movil_ctl.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movil_ctl.data.entities.NucleosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NucleoDao {

    @Query("select * from nucleos")
    fun getAllNucleos(): List<NucleosEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createNucleos(nucleos: List<NucleosEntity>)

    @Query("DELETE FROM nucleos")
    fun deleteAll()
}