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
    @SerializedName("equipoId") val equipo: EquipoStructure,
    @SerializedName("__v") val version: Int
)

fun OperadorStructure.toLocalOperador(): OperadorEntity {
    return OperadorEntity(
        id = this.id,
        cedulaOperador = this.cedulaOperador,
        nombreOperador = this.nombreOperador,
        equiposId = this.equipo.id
    )
}
