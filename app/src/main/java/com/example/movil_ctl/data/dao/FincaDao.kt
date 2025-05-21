package com.example.movil_ctl.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movil_ctl.data.entities.FincasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FincaDao {
    @Query("select * from fincas")
    fun getAllFincas(): Flow<List<FincasEntity>>


    @Query("SELECT * FROM fincas WHERE nucleo_id = :nucleoId")
    fun getFincasByNucleo(nucleoId: String): Flow<List<FincasEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createFincas(fincas: List<FincasEntity>)

    @Query("DELETE FROM fincas")
    suspend fun deleteAll()
}