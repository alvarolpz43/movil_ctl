package com.example.movil_ctl.data.dao

import androidx.room.Query
import com.example.movil_ctl.data.entities.RegistroEntity
import kotlinx.coroutines.flow.Flow

interface RegistroDao : BaseDao<RegistroDao> {
    @Query("SELECT * FROM registros")
    fun getAll(): Flow<List<RegistroEntity>>

    @Query("SELECT * FROM registros WHERE id = :id")
    suspend fun getById(id: Int): RegistroEntity?

    @Query("SELECT * FROM registros WHERE fecha BETWEEN :startDate AND :endDate")
    fun getByDateRange(startDate: String, endDate: String): Flow<List<RegistroEntity>>
}