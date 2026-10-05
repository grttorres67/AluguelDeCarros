package com.example.alugueldecarrosramyres.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alugueldecarrosramyres.data.local.entity.ClientCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateClient(client: ClientCacheEntity): Long

    @Query("SELECT * FROM clients_cache WHERE contactId = :contactId LIMIT 1")
    suspend fun getClientByContactId(contactId: String): ClientCacheEntity?

    @Query("SELECT * FROM clients_cache WHERE id = :id")
    suspend fun getClientById(id: Long): ClientCacheEntity?

    @Query("SELECT * FROM clients_cache")
    fun getAllClients(): Flow<List<ClientCacheEntity>>
}
