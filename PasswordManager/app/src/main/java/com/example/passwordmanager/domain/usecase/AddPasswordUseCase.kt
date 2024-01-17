package com.example.passwordmanager.domain.usecase

import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.repository.PasswordRepository

class AddPasswordUseCase(private val repository: PasswordRepository) {

    suspend fun execute(password: Password) {
        repository.addPassword(password)
    }
}