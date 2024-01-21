package com.example.passwordmanager.domain.usecase

import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.repository.PasswordRepository
import kotlinx.coroutines.flow.Flow

class GetAllPasswordsUseCase(private val repository: PasswordRepository) {

    operator fun invoke(): Flow<List<Password>> {
        return repository.getAll()
    }

}