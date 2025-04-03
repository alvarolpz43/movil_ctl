package com.example.movil_ctl.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movil_ctl.data.entities.ContratistaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContratistaDao {
    @Query("SELECT * FROM contratistas")
    fun getAll(): Flow<List<ContratistaEntity>>  // Para observación continua

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contratistas: List<ContratistaEntity>)  // Solo inserción

    @Query("SELECT * FROM contratistas WHERE id = :id")
    suspend fun getById(id: String): ContratistaEntity?

    @Query("DELETE FROM contratistas")
    suspend fun deleteAll()
}