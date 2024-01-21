package com.example.passwordmanager.presentation.details

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.usecase.GetPasswordUseCase
import com.example.passwordmanager.domain.util.ActionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getPasswordUseCase: GetPasswordUseCase
): ViewModel() {

    private val _state = MutableLiveData<ActionState>(ActionState.PENDING)
    val state: LiveData<ActionState> get() = _state

    private val _password = MutableLiveData<Password>()
    val password: LiveData<Password> = _password

    private val _showPasswordState = MutableLiveData<Boolean>(false)
    val showPasswordState: LiveData<Boolean> = _showPasswordState

    fun loadPassword(id: Int) {
        viewModelScope.launch {
            val password = getPasswordUseCase(id)

            if (password == null) {
                _state.value = ActionState.FAIL
            } else {
                _state.value = ActionState.SUCCESS
                _password.value = password
            }
        }
    }

    fun toggleShowPasswordState() {
        _showPasswordState.postValue(!showPasswordState.value!!)
    }

}