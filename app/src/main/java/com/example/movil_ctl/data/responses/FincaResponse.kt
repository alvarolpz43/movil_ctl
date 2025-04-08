package com.example.movil_ctl.data.responses

import com.example.movil_ctl.data.entities.FincasEntity
import com.google.gson.annotations.SerializedName

data class FincaResponse(
    val success: Boolean,
    val data: List<FincaStructure>
)

data class FincaStructure(
    @SerializedName("_id") val id: String,
    @SerializedName("codeFinca") val codeFinca: String,
    @SerializedName("nombreFinca") val nombreFinca: String,
    @SerializedName("nucleoId") val nucleoId: String,
)

//fun FincaStructure.toLocalFinca(): FincasEntity {
//    return FincasEntity(
//        id = this.id,
//        codFinca = this.codeFinca,
//        nombre = this.nombreFinca,
//        nucleoId = this.nucleoId
//    )
//}
