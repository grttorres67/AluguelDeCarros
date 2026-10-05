package com.example.alugueldecarrosramyres.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VehicleDto(
    @SerializedName("id") val id: Long,
    @SerializedName("marca") val marca: String,
    @SerializedName("modelo") val modelo: String,
    @SerializedName("placa") val placa: String,
    @SerializedName("ano") val ano: Int,
    @SerializedName("valorDiaria") val valorDiaria: Double,
    @SerializedName("status") val status: String
)
