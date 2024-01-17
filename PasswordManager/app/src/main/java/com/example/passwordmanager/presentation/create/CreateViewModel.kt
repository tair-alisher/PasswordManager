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

    private val _state = MutableLiveData<ActionResult<Error>>()
    val state: LiveData<ActionResult<Error>> = _state

    init {
        _state.value = ActionResult()
    }

    fun addPassword(name: String, link: String, login: String, password: String) {
        if (name.isEmpty())
            _state.postValue(
                _state.value?.copy(
                    state = ActionState.FAIL,
                    data = Error(Field.NAME, FieldError.EMPTY),
                    error = "Name cannot be empty")
            )

        if (password.isEmpty())
            _state.postValue(
                _state.value?.copy(
                    state = ActionState.FAIL,
                    data = Error(Field.PASSWORD, FieldError.EMPTY),
                    error = "Password cannot be empty")
            )

        if (login.isEmpty())
            _state.postValue(
                _state.value?.copy(
                    state = ActionState.FAIL,
                    data = Error(Field.LOGIN, FieldError.EMPTY),
                    error = "Login cannot be empty")
            )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                addPasswordUseCase.execute(Password(
                    title = name, link = link, login = login, password = password
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