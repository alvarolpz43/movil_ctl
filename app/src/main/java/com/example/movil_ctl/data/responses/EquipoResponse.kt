package com.example.movil_ctl.data.responses

import com.example.movil_ctl.data.entities.EquipoEntity
import com.google.gson.annotations.SerializedName

data class EquipoResponse(
    val success: Boolean,
    val data: List<EquipoStructure>
)

data class EquipoStructure(
    @SerializedName("_id") val id: String,
    @SerializedName("nombreEquipo") val nombreEquipo: String,
    @SerializedName("serieEquipo") val serieEquipo: String,
    @SerializedName("contratistaId") val contratista: ContratistaEstructure,
    @SerializedName("__v") val version: Int
)

//fun     EquipoStructure.toLocalEquipo(): EquipoEntity {
//    return EquipoEntity(
//        id = this.id,
//        nombreEquipo = this.nombreEquipo,
//        serieEquipo = this.serieEquipo,
//        contratistasId = this.contratista.data.
//    )
//}
