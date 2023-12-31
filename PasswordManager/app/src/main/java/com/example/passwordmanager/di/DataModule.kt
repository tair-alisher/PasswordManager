package com.example.passwordmanager.di

import android.content.Context
import com.example.passwordmanager.data.shared_pref.SharedPrefStorage
import com.example.passwordmanager.data.shared_pref.SharedPrefStorageImpl
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
}