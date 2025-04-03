package com.example.movil_ctl.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "nucleos",
    foreignKeys = [ForeignKey(
        entity = ZonasEntity::class,
        parentColumns = ["id"],
        childColumns = ["zona_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class NucleosEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "cod_nucleo") val codNucleo: String,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "zona_id") val zonaId: String
)
