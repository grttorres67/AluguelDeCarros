package com.example.alugueldecarrosramyres.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.alugueldecarrosramyres.data.local.entity.RentalEntity
import com.example.alugueldecarrosramyres.data.local.relation.RentalWithVehicleAndClient
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRental(rental: RentalEntity): Long

    @Transaction
    @Query("SELECT * FROM rentals WHERE status = 'ATIVA'")
    fun getActiveRentalsWithDetails(): Flow<List<RentalWithVehicleAndClient>>

    @Query("UPDATE rentals SET status = :status WHERE id = :id")
    suspend fun updateRentalStatus(id: Long, status: String)
}
