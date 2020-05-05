package com.aengussong.beddit.model

sealed class State(val message: String?) {
    data class RequestError(val errorMessage: String) : State(errorMessage)
}