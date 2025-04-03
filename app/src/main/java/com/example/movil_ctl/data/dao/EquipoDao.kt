package com.example.movil_ctl.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movil_ctl.data.entities.EquipoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipoDao {
    @Query("SELECT * FROM equipos")
    fun getAll(): Flow<List<EquipoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(equipos: List<EquipoEntity>)

    @Query("SELECT * FROM equipos WHERE contratistas_id = :codigo")
    suspend fun getByCodigo(codigo: String): EquipoEntity?

    @Query("DELETE FROM equipos")
    suspend fun deleteAll()
}