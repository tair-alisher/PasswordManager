package com.example.passwordmanager.presentation.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.usecase.AddPasswordUseCase
import com.example.passwordmanager.domain.util.ActionResult
import com.example.passwordmanager.domain.util.ActionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val addPasswordUseCase: AddPasswordUseCase
) : ViewModel() {

    private val _formData = MutableLiveData<Password>()

    private val _state = MutableLiveData<ActionResult<Error>>()
    val state: LiveData<ActionResult<Error>> = _state

    private val _formErrors = MutableLiveData<CreateFormState>()
    val formErrors: LiveData<CreateFormState> = _formErrors

    init {
        _formData.value = Password(0, "", "", "", "")
        _state.value = ActionResult()
        _formErrors.value = CreateFormState()
    }

    fun updateTitle(title: String) {
        _formData.postValue(_formData.value?.copy(title = title))
    }

    fun updateLink(link: String) {
        _formData.postValue(_formData.value?.copy(link = link))
    }

    fun updateLogin(login: String) {
        _formData.postValue(_formData.value?.copy(login = login))
    }

    fun updatePassword(password: String) {
        _formData.postValue(_formData.value?.copy(password = password))
    }

    private fun validate() {
        _formErrors.value = CreateFormState()

        if (_formData.value!!.title.isEmpty()) {
            _formErrors.value = _formErrors.value?.copy(nameError = "Name cannot be empty")
        }

        if (_formData.value!!.login.isEmpty()) {
            _formErrors.value = _formErrors.value?.copy(loginError = "Login cannot be empty")
        }

        if (_formData.value!!.password.isEmpty()) {
            _formErrors.value = _formErrors.value?.copy(passwordError = "Password cannot be empty")
        }
    }

    fun addPassword() {
        validate()

        val hasError = listOf(
            _formErrors.value?.nameError,
            _formErrors.value?.loginError,
            _formErrors.value?.passwordError
        ).any { !it.isNullOrEmpty() }

        if (hasError) return

        viewModelScope.launch {
            try {
                addPasswordUseCase.execute(Password(
                    title = _formData.value!!.title,
                    link = _formData.value!!.link,
                    login = _formData.value!!.login,
                    password = _formData.value!!.password
                ))

                _state.postValue(_state.value?.copy(state = ActionState.SUCCESS))
            } catch (ex: Exception) {
                _state.postValue(
                    _state.value?.copy(
                        state = ActionState.FAIL,
                        error = ex.message ?: "Failed to add password")
                )
            }
        }
    }

}

data class CreateFormState(
    val nameError: String = "",
    val loginError: String = "",
    val passwordError: String = ""
)