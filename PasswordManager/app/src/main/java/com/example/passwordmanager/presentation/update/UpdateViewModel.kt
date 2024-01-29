package com.example.passwordmanager.presentation.update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.usecase.GetPasswordUseCase
import com.example.passwordmanager.domain.usecase.UpdatePasswordUseCase
import com.example.passwordmanager.domain.util.ActionState
import com.example.passwordmanager.domain.util.ScreenState
import com.example.passwordmanager.presentation.utils.Field
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val getPasswordUseCase: GetPasswordUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {

    private val _password = MutableLiveData<Password>()
    val password: LiveData<Password> = _password

    private val _formData = MutableLiveData<Password>()

    private val _state = MutableLiveData<ScreenState>(ScreenState(ActionState.PENDING))
    val state: LiveData<ScreenState> = _state

    private val _formErrors = MutableLiveData<UpdateFormState>()
    val formErrors: LiveData<UpdateFormState> = _formErrors

    init {
        _formData.value = Password(0, "", "", "", "")
    }

    fun loadPassword(id: Int) {
        viewModelScope.launch {
            try {
                val password = getPasswordUseCase(id)

                _state.value = state.value?.copy(state = ActionState.SUCCESS)
                _password.value = password
            }
            catch (ex: Exception) {
                _state.value = state.value?.copy(state = ActionState.FAIL, message = "Failed to get password information")
            }
        }
    }

    fun updateFormData(field: Field, value: String) {
        when (field) {
            Field.NAME -> {
                _formData.postValue(_formData.value?.copy(title = value))
            }
            Field.LOGIN -> {
                _formData.postValue(_formData.value?.copy(login = value))
            }
            Field.LINK -> {
                _formData.postValue(_formData.value?.copy(link = value))
            }
            Field.PASSWORD -> {
                _formData.postValue(_formData.value?.copy(password = value))
            }
        }
    }

    private fun validate() {
        _formErrors.value = UpdateFormState()

        if (_formData.value!!.title.isEmpty()) {
            _formErrors.value = _formErrors.value?.copy(nameError = "Name cannot be empty")
        }

        if (_formData.value?.login?.isEmpty() != false) {
            _formErrors.value = _formErrors.value?.copy(loginError = "Login cannot be empty")
        }

        if (_formData.value?.password?.isEmpty() != false) {
            _formErrors.value = _formErrors.value?.copy(passwordError = "Password cannot be empty")
        }
    }

    fun saveChanges() {
        validate()

        val hasError = listOf(
            _formErrors.value?.nameError,
            _formErrors.value?.loginError,
            _formErrors.value?.passwordError
        ).any { !it.isNullOrEmpty() }

        if (hasError) return

        viewModelScope.launch {
            _formData.value = _formData.value?.copy(id = password.value?.id ?: 0)
            try {
                updatePasswordUseCase(_formData.value!!)
                _state.value = state.value?.copy(state = ActionState.MOVE_NEXT, message = "")
            }
            catch (ex: Exception) {
                _state.value = state.value?.copy(state = ActionState.FAIL, message = "Failed to save changes")
            }
        }
    }

}

data class UpdateFormState(
    val nameError: String = "",
    val loginError: String = "",
    val passwordError: String = ""
)