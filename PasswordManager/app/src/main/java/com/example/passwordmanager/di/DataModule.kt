package com.example.passwordmanager.di

import android.content.Context
import androidx.room.Room
import com.example.passwordmanager.data.db.Database
import com.example.passwordmanager.data.db.dao.PasswordDao
import com.example.passwordmanager.data.db.repository.PasswordRepositoryImpl
import com.example.passwordmanager.data.shared_pref.SharedPrefStorage
import com.example.passwordmanager.data.shared_pref.SharedPrefStorageImpl
import com.example.passwordmanager.domain.repository.PasswordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideSharedPrefStorage(@ApplicationContext context: Context): SharedPrefStorage = SharedPrefStorageImpl(context)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "at_password_manager_db").build()
    }

    @Provides
    @Singleton
    fun providePasswordDao(database: Database): PasswordDao = database.passwordDao()

    @Provides
    @Singleton
    fun providePasswordRepository(passwordDao: PasswordDao): PasswordRepository = PasswordRepositoryImpl(passwordDao)
}