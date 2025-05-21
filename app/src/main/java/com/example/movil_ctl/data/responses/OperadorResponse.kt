package com.example.movil_ctl.data.responses

import com.example.movil_ctl.data.entities.OperadorEntity
import com.google.gson.annotations.SerializedName

data class OperadorResponse(
    val success: Boolean,
    val data: List<OperadorStructure>
)

data class OperadorStructure(
    @SerializedName("_id") val id: String,
    @SerializedName("numCedula") val cedulaOperador: String,
    @SerializedName("nameOperador") val nombreOperador: String,
    @SerializedName("equipoId") val equipo: EquipoEnOperador,
    @SerializedName("__v") val version: Int
)


data class EquipoEnOperador(
    @SerializedName("_id") val id: String,
    @SerializedName("nombreEquipo") val nombreEquipo: String,
    @SerializedName("serieEquipo") val serieEquipo: String,
    @SerializedName("contratistaId") val contratista: ContratistaEnEquipo
)

data class ContratistaEnEquipo(
    @SerializedName("_id") val id: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("estado") val estado: Boolean
)
//fun OperadorStructure.toLocalOperador(): OperadorEntity {
//    return OperadorEntity(
//        id = this.id,
//        cedulaOperador = this.cedulaOperador,
//        nombreOperador = this.nombreOperador,
//        equiposId = this.equipo.id
//    )
//}
