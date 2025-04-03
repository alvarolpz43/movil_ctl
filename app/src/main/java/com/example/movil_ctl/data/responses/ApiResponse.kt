package com.example.movil_ctl.data.responses

data class ApiResponse<T>(
    val success: Boolean,
    val data: List<T>
)
