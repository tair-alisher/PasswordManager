package com.example.passwordmanager.domain.usecase

import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.repository.PasswordRepository

class UpdatePasswordUseCase(private val repository: PasswordRepository) {

    suspend operator fun invoke(password: Password) {
        repository.update(password)
    }

}