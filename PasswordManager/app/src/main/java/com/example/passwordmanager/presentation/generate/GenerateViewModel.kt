package com.example.passwordmanager.presentation.generate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.passwordmanager.domain.usecase.GeneratePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val generatePasswordUseCase: GeneratePasswordUseCase
) : ViewModel() {
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    init {
        _password.value = ""
    }

    fun generate(length: Int, includeSymbols: Boolean, includeDigits: Boolean) {
        val generatedPassword = generatePasswordUseCase(
            length,
            includeSymbols,
            includeDigits
        )

        _password.value = generatedPassword
    }
}