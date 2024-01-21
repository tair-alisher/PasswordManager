package com.example.passwordmanager.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.usecase.GetAllPasswordsUseCase
import com.example.passwordmanager.domain.util.ActionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPasswordsUseCase: GetAllPasswordsUseCase
) : ViewModel() {

    private val _state = MutableLiveData<ActionState>(ActionState.PENDING)
    val state: LiveData<ActionState> get() = _state

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _passwords = MutableStateFlow<List<Password>>(emptyList())
    val passwords: StateFlow<List<Password>> get() = _passwords

    fun loadPasswords() {
        viewModelScope.launch {
             getAllPasswordsUseCase().catch {
                 _state.value = ActionState.FAIL
                 _error.value = "Failed to retrieve data"
             }.collect { items ->
                 _state.value = ActionState.SUCCESS
                 _passwords.value = items
            }
        }
    }

    companion object {
        private const val TAG: String = "HomeViewModel"
    }

}