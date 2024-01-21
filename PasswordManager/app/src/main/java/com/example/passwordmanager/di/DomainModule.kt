package com.example.passwordmanager.di

import com.example.passwordmanager.domain.repository.PasswordRepository
import com.example.passwordmanager.domain.usecase.AddPasswordUseCase
import com.example.passwordmanager.domain.usecase.GetAllPasswordsUseCase
import com.example.passwordmanager.domain.usecase.GetPasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetAllPasswordsUseCase(repository: PasswordRepository): GetAllPasswordsUseCase {
        return GetAllPasswordsUseCase(repository)
    }

    @Provides
    fun provideAddPasswordUseCase(repository: PasswordRepository): AddPasswordUseCase {
        return AddPasswordUseCase(repository)
    }

    @Provides
    fun provideGetPasswordUseCase(repository: PasswordRepository): GetPasswordUseCase {
        return GetPasswordUseCase(repository)
    }

}