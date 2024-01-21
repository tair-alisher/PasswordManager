package com.example.passwordmanager.domain.usecase

import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.repository.PasswordRepository

class GetPasswordUseCase(private val repository: PasswordRepository) {

    suspend operator fun invoke(id: Int): Password {
        return repository.getById(id)
    }

}