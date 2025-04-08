package com.example.movil_ctl.data.responses

import com.example.movil_ctl.data.entities.ZonasEntity
import com.google.gson.annotations.SerializedName

data class ZonaResponse(
    val success: Boolean,
    val data: List<ZonaStructure>
)

data class ZonaStructure(
    @SerializedName("_id") val id: String,
    @SerializedName("codeZona") val codeZona: String,
    @SerializedName("nombreZona") val nombreZona: String
)

fun ZonaStructure.toLocalZona(): ZonasEntity {
    return ZonasEntity(
        id = this.id,
        codZona = this.codeZona,
        nombre = this.nombreZona
    )
}
