package com.example.alugueldecarrosramyres.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alugueldecarrosramyres.data.local.dao.ClientDao
import com.example.alugueldecarrosramyres.data.local.dao.RentalDao
import com.example.alugueldecarrosramyres.data.local.dao.VehicleDao
import com.example.alugueldecarrosramyres.data.local.entity.ClientCacheEntity
import com.example.alugueldecarrosramyres.data.local.entity.RentalEntity
import com.example.alugueldecarrosramyres.data.local.entity.VehicleEntity

@Database(
    entities = [VehicleEntity::class, ClientCacheEntity::class, RentalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun clientDao(): ClientDao
    abstract fun rentalDao(): RentalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aluguel_carros_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
