package com.example.movil_ctl.data.responses

import com.example.movil_ctl.data.entities.ContratistaEntity
import com.google.gson.annotations.SerializedName

data class ContratistaResponse(
    val success: Boolean,
    val data: List<ContratistaEstructure>
)

data class ContratistaEstructure(
    @SerializedName("_id") val id: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("estado") val estado: Boolean,
    @SerializedName("__v") val version: Int
)


fun ContratistaEstructure.toLocalContratista(): ContratistaEntity {
    return ContratistaEntity(
        id = this.id,
        nombre = this.nombre,
        estado = this.estado
    )
}