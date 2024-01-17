package com.example.passwordmanager.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.domain.model.Password
import com.example.passwordmanager.domain.usecase.GetAllPasswordsUseCase
import com.example.passwordmanager.domain.util.ActionResult
import com.example.passwordmanager.domain.util.ActionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPasswordsUseCase: GetAllPasswordsUseCase
) : ViewModel() {

    private val _state = MutableLiveData<ActionResult<List<Password>>>()
    val state: LiveData<ActionResult<List<Password>>> = _state

    init {
        Log.d(TAG, "init")
        _state.value = ActionResult(data = emptyList())

        viewModelScope.launch(Dispatchers.IO) {
            getAllPasswordsUseCase.execute()
                .catch { ex ->
                    Log.d(TAG, "Failed to retrieve data: ${ex.message}. ${ex.stackTrace}")
                    _state.postValue(_state.value?.copy(state = ActionState.FAIL, error = "Failed to retrieve data"))
                }
                .collect { items -> _state.postValue(_state.value?.copy(state = ActionState.SUCCESS, data = items)) }

            Log.d(TAG, "SUCCESS")
        }
    }

    companion object {
        private const val TAG: String = "HomeViewModel"
    }

}