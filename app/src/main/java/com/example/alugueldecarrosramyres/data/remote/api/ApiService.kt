package com.example.alugueldecarrosramyres.data.remote.api

import com.example.alugueldecarrosramyres.data.remote.dto.RentalDto
import com.example.alugueldecarrosramyres.data.remote.dto.VehicleDto
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("vehicles")
    suspend fun getVehicles(): Response<List<VehicleDto>>

    @POST("vehicles")
    suspend fun syncVehicle(@Body vehicle: VehicleDto): Response<VehicleDto>

    @GET("rentals")
    suspend fun getRentals(): Response<List<RentalDto>>

    @POST("rentals")
    suspend fun syncRental(@Body rental: RentalDto): Response<RentalDto>

    companion object {
        private const val BASE_URL = "https://mock.alugueldecarrosramyres.com/api/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
