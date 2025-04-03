package com.example.movil_ctl.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "equipos",
    foreignKeys = [ForeignKey(
        entity = ContratistaEntity::class,
        parentColumns = ["id"],
        childColumns = ["contratistas_id"],
        onDelete = ForeignKey.CASCADE
    )])

data class EquipoEntity(
    @ColumnInfo(name = "_id") val id: String,
    @ColumnInfo(name = "nombre_equipo") val nombreEquipo: String,
    @ColumnInfo(name = "serie_equipo") val serieEquipo: String,
    @ColumnInfo(name = "contratistas_id") val contratistasId: String
)
