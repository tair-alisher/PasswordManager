package com.example.passwordmanager.domain.usecase

import com.example.passwordmanager.domain.repository.PasswordRepository

class DeletePasswordUseCase(private val repository: PasswordRepository) {

    suspend operator fun invoke(id: Int) {
        return repository.deletePassword(id)
    }

}