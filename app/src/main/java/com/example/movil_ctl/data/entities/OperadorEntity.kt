package com.example.movil_ctl.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "operadores",
    foreignKeys = [ForeignKey(
        entity = EquipoEntity::class,
        parentColumns = ["id"],
        childColumns = ["equipos_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class OperadorEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "cedula_operador") val cedulaOperador: String,
    @ColumnInfo(name = "nombre_operador") val nombreOperador: String,
    @ColumnInfo(name = "equipos_id") val equiposId: String
)

