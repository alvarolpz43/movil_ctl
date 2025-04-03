package com.example.movil_ctl.data.responses

import com.example.movil_ctl.data.entities.EspecieEntity
import com.google.gson.annotations.SerializedName

data class EspecieResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("nombreEspecie") val nombre: String,
)

fun EspecieResponse.toLocalEspecie(): EspecieEntity {
    return EspecieEntity(
        id = this.id,
        nombre = this.nombre
    )
}
