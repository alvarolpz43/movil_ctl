package com.example.movil_ctl.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movil_ctl.data.entities.OperadorEntity
import kotlinx.coroutines.flow.Flow



@Dao
interface OperadorDao {
    @Query("select * from operadores where equipos_id = :equiposId ")
    fun getOperadorByEquipo(equiposId: Int): Flow<List<OperadorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createOperadores(operadores: List<OperadorEntity>)

    @Query("DELETE FROM operadores")
    suspend fun deleteAll()

}