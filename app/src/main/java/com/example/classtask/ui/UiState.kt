package com.example.classtask.ui

sealed interface UiState<out T> {

    data object Loading : UiState<Nothing>

    data object Refreshing : UiState<Nothing>

    data class Success<out T>(val data: T) : UiState<T>

    data class Error(val message: String) : UiState<Nothing>
}
