package com.example.movil_ctl.data.responses

import com.example.movil_ctl.data.entities.NucleosEntity
import com.google.gson.annotations.SerializedName

data class NucleoResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("codeNucleo") val codeNucleo: String,
    @SerializedName("nombreNucleo") val nombreNucleo: String,
    @SerializedName("zonaId") val zonaId: String
)

fun NucleoResponse.toLocalNucleo(): NucleosEntity {
    return NucleosEntity(
        id = this.id,
        codNucleo = this.codeNucleo,
        nombre = this.nombreNucleo,
        zonaId = this.zonaId
    )
}