package com.example.passwordmanager.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.usecase.DeletePasswordUseCase
import com.example.passwordmanager.domain.usecase.GetPasswordUseCase
import com.example.passwordmanager.domain.util.ActionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getPasswordUseCase: GetPasswordUseCase,
    private val deletePasswordUseCase: DeletePasswordUseCase
): ViewModel() {

    private val _state = MutableLiveData<ActionState>(ActionState.PENDING)
    val state: LiveData<ActionState> get() = _state

    private val _deleteState = MutableLiveData<DeleteState>()
    val deleteState: LiveData<DeleteState> = _deleteState

    private val _password = MutableLiveData<Password>()
    val password: LiveData<Password> = _password

    private val _showPasswordState = MutableLiveData<Boolean>(false)
    val showPasswordState: LiveData<Boolean> = _showPasswordState

    init {
        _deleteState.value = DeleteState.UNDEFINED
    }

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

    fun deletePassword() {
        if (_password.value == null) {
            _deleteState.postValue(DeleteState.SUCCESS)
            return
        }

        viewModelScope.launch {
            try {
                deletePasswordUseCase(_password.value!!.id)
                _deleteState.value = DeleteState.SUCCESS
            }
            catch (ex: Exception) {
                _deleteState.value = DeleteState.FAIL
            }
        }
    }
}

enum class DeleteState {
    UNDEFINED, SUCCESS, FAIL
}