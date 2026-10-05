package com.example.alugueldecarrosramyres.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RentalDto(
    @SerializedName("id") val id: Long,
    @SerializedName("vehicleId") val vehicleId: Long,
    @SerializedName("clientId") val clientId: Long,
    @SerializedName("dataSaida") val dataSaida: Long,
    @SerializedName("dataPrevistaEntrega") val dataPrevistaEntrega: Long,
    @SerializedName("valorTotalEstimado") val valorTotalEstimado: Double,
    @SerializedName("status") val status: String
)
