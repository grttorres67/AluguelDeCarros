package com.example.alugueldecarrosramyres.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients_cache")
data class ClientCacheEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val contactId: String,
    val nome: String,
    val telefone: String
)
