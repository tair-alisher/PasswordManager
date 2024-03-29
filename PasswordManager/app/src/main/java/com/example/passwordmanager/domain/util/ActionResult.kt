package com.example.passwordmanager.domain.util

data class ActionResult<T>(
    val state: ActionState = ActionState.UNDEFINED,
    val data: T? = null,
    val error: String? = null)

enum class ActionState {
    UNDEFINED, SUCCESS, FAIL, PENDING, MOVE_NEXT
}

data class ScreenState(
    val state: ActionState = ActionState.UNDEFINED,
    val message: String = ""
)