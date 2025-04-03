package com.example.movil_ctl.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "fincas",
    foreignKeys = [ForeignKey(
        entity = NucleosEntity::class,
        parentColumns = ["id"],
        childColumns = ["nucleo_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class FincasEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "cod_finca") val codFinca: String,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "nucleo_id") val nucleoId: String
)
