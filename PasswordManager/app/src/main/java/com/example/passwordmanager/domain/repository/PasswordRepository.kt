package com.example.passwordmanager.domain.repository

import com.example.passwordmanager.domain.model.Password
import kotlinx.coroutines.flow.Flow

interface PasswordRepository {
    fun getAll(): Flow<List<Password>>

    suspend fun addPassword(password: Password)

    suspend fun deletePassword(id: Int)

    fun searchPassword(searchVal: String): Flow<List<Password>>
}