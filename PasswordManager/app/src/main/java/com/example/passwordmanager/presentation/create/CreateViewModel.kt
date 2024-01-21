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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val addPasswordUseCase: AddPasswordUseCase
) : ViewModel() {

    private val _formData = MutableLiveData<Password>()
    val formData: LiveData<Password> get() = _formData

    private val _state = MutableLiveData<ActionResult<Error>>()
    val state: LiveData<ActionResult<Error>> = _state

    init {
        _formData.value = Password(0, "", "", "", "")
        _state.value = ActionResult()
    }

    fun updateTitle(title: String) {
        _formData.postValue(formData.value?.copy(title = title))

        if (title.isEmpty()) {
            _state.postValue(state.value?.copy(
                state = ActionState.FAIL,
                data = Error(Field.NAME, FieldError.EMPTY),
                error = "Name cannot be empty"))
        }
    }

    fun updateLink(link: String) {
        _formData.postValue(formData.value?.copy(link = link))
    }

    fun updateLogin(login: String) {
        _formData.postValue(formData.value?.copy(login = login))

        if (login.isEmpty()) {
            _state.postValue(state.value?.copy(
                state = ActionState.FAIL,
                data = Error(Field.LOGIN, FieldError.EMPTY),
                error = "Login cannot be empty"))
        }
    }

    fun updatePassword(password: String) {
        _formData.postValue(formData.value?.copy(password = password))

        if (password.isEmpty()) {
            _state.postValue(state.value?.copy(
                state = ActionState.FAIL,
                data = Error(Field.PASSWORD, FieldError.EMPTY),
                error = "Password cannot be empty"))
        }
    }

    fun addPassword() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addPasswordUseCase.execute(Password(
                    title = formData.value!!.title,
                    link = formData.value!!.link,
                    login = formData.value!!.login,
                    password = formData.value!!.password
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

enum class Field {
    NAME, LINK, LOGIN, PASSWORD
}

enum class FieldError {
    EMPTY,
}

data class Error(
    val field: Field? = null,
    val error: FieldError? = null
)