package com.example.alugueldecarrosramyres.data.repository

import com.example.alugueldecarrosramyres.data.local.dao.ClientDao
import com.example.alugueldecarrosramyres.data.local.entity.ClientCacheEntity
import kotlinx.coroutines.flow.Flow

class ClientRepository(private val clientDao: ClientDao) {
    val allClients: Flow<List<ClientCacheEntity>> = clientDao.getAllClients()

    suspend fun insertOrUpdateClient(client: ClientCacheEntity): Long {
        val existing = clientDao.getClientByContactId(client.contactId)
        return if (existing != null) {
            val updated = existing.copy(nome = client.nome, telefone = client.telefone)
            clientDao.insertOrUpdateClient(updated)
            existing.id
        } else {
            clientDao.insertOrUpdateClient(client)
        }
    }

    suspend fun getClientById(id: Long): ClientCacheEntity? {
        return clientDao.getClientById(id)
    }
}
