package com.example.movil_ctl.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "turnos",
    foreignKeys = [ForeignKey(
        entity = ContratistaEntity::class,
        parentColumns = ["id"],
        childColumns = ["contratistas_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TurnoEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "hora_inicio") val horaInicio: String,
    @ColumnInfo(name = "hora_fin") val horaFin: String,
    @ColumnInfo(name = "contratistas_id") val contratistasId: String
)