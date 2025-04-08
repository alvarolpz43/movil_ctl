package com.example.movil_ctl.data.responses

import com.example.movil_ctl.data.entities.TurnoEntity

data class TurnoResponse(
    val success: Boolean,
    val data: List<TurnoStructure>
)

data class TurnoStructure(
    val _id: String?,
    val nombre: String?,
    val hora_inicio: String?,
    val hora_fin: String?,
    val contratistas_id: ContratistaEstructure?,
)

//fun TurnoStructure.toLocalTurno(): TurnoEntity {
//    return TurnoEntity(
//        id = this._id,
//        nombre = this.nombre,
//        horaInicio = this.hora_inicio,
//        horaFin = this.hora_fin,
//        contratistasId = this.contratistas_id
//    )
//}
