package com.example.movil_ctl.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "registros",
    foreignKeys = [
        ForeignKey(
            entity = ZonasEntity::class,
            parentColumns = ["id"],
            childColumns = ["zona_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = NucleosEntity::class,
            parentColumns = ["id"],
            childColumns = ["nucleo_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = FincasEntity::class,
            parentColumns = ["id"],
            childColumns = ["finca_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = ContratistaEntity::class,
            parentColumns = ["id"],
            childColumns = ["contratista_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = EspecieEntity::class,
            parentColumns = ["id"],
            childColumns = ["especie_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = TurnoEntity::class,
            parentColumns = ["id"],
            childColumns = ["turno_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = EquipoEntity::class,
            parentColumns = ["id"],
            childColumns = ["equipo_id"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = OperadorEntity::class,
            parentColumns = ["id"],
            childColumns = ["operador_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class RegistroEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "equipo_id") val codigoEquipo: String,
    @ColumnInfo(name = "operador_id") val cedulaOperador: String,
    @ColumnInfo(name = "zona_id") val zonaId: Int,
    @ColumnInfo(name = "nucleo_id") val nucleoId: Int,
    @ColumnInfo(name = "finca_id") val fincaId: Int,
    @ColumnInfo(name = "contratista_id") val contratistaId: Int,
    @ColumnInfo(name = "fecha") val fecha: String, // Usar TypeConverter para Date si es necesario
    @ColumnInfo(name = "especie_id") val especieId: Int,
    @ColumnInfo(name = "lote") val lote: String,
    @ColumnInfo(name = "turno_id") val turnoId: Int,
    @ColumnInfo(name = "pendiente") val pendiente: Int,
    @ColumnInfo(name = "L_programado") val lProgramado: Float,
    @ColumnInfo(name = "L_efectivo") val lEfectivo: Float,
    @ColumnInfo(name = "L_paradas_mecanicas") val lParadasMecanicas: Float,
    @ColumnInfo(name = "ref_repuesto") val refRepuesto: String,
    @ColumnInfo(name = "alistamiento") val alistamiento: Float,
    @ColumnInfo(name = "tanqueo") val tanqueo: Float,
    @ColumnInfo(name = "alimentacion") val alimentacion: Float,
    @ColumnInfo(name = "L_otra_parada") val lOtraParada: Float,
    @ColumnInfo(name = "motivos_parada") val motivosParada: String,
    @ColumnInfo(name = "Uuso_vinche") val usoVinche: Float,
    @ColumnInfo(name = "novedades") val novedades: String,
    @ColumnInfo(name = "suelo") val suelo: String, // Enum en SQLite se maneja como String
    @ColumnInfo(name = "horas_parada") val horasParada: Float,
    @ColumnInfo(name = "eficiencia") val eficiencia: Float,
    @ColumnInfo(name = "diametro_medio") val diametroMedio: Float,
    @ColumnInfo(name = "productividad_m3_h") val productividadM3H: Float,
    @ColumnInfo(name = "esp_reparacion") val espReparacion: String,
    @ColumnInfo(name = "reparacion") val reparacion: String,
    @ColumnInfo(name = "tipo_maquina") val tipoMaquina: String // Enum en SQLite se maneja como String
)
