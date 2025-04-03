package com.example.movil_ctl.core

import androidx.room.Query
import com.example.movil_ctl.data.responses.ApiResponse
import com.example.movil_ctl.data.responses.ContratistaResponse
import com.example.movil_ctl.data.responses.EquipoResponse
import com.example.movil_ctl.data.responses.EspecieResponse
import com.example.movil_ctl.data.responses.FincaResponse
import com.example.movil_ctl.data.responses.NucleoResponse
import com.example.movil_ctl.data.responses.OperadorResponse
import com.example.movil_ctl.data.responses.TurnoResponse
import com.example.movil_ctl.data.responses.ZonaResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("contratistas")
    suspend fun getContratistas(): Response<List<ContratistaResponse>>

    @GET("equipos")
    suspend fun getEquipos(): Response<List<EquipoResponse>>

    @GET("operadores")
    suspend fun getOperadores(): Response<ApiResponse<List<OperadorResponse>>>

    @GET("turnos")
    suspend fun getTurnos(): ApiResponse<List<TurnoResponse>>

    @GET("especies")
    suspend fun getEspecies(): ApiResponse<List<EspecieResponse>>

    @GET("fincas")
    suspend fun getFincas(): ApiResponse<List<FincaResponse>>

    @GET("nucleos")
    suspend fun getNucleos(): ApiResponse<List<NucleoResponse>>

    @GET("zonas")
    suspend fun getZonas(): ApiResponse<List<ZonaResponse>>


//    @GET("registros")
//    suspend fun getRegistros(@Query("lastSync") lastSync: String?): Response<List<RegistroResponse>>

//    @POST("registros/sync")
//    suspend fun sendRegistros(@Body registros: List<RegistroSyncRequest>): Response<SyncResponse>
}