package com.example.passwordmanager.domain.usecase

import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.repository.PasswordRepository
import kotlinx.coroutines.flow.Flow

class SearchPasswordsUseCase(private val repository: PasswordRepository) {

    operator fun invoke(searchVal: String): Flow<List<Password>> {
        return repository.searchPassword("%${searchVal.trim()}%")
    }

}