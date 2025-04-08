package com.example.movil_ctl.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movil_ctl.data.entities.EspecieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EspecieDao {

    @Query("select * from especies")
    fun getAllEspecies(): Flow<List<EspecieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createEspecies(especies: List<EspecieEntity>)

    @Query("DELETE FROM especies")
    fun deleteAll()
}