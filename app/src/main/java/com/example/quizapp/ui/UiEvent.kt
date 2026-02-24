package com.example.quizapp.ui

sealed interface UiEvent {
    data object NavigateBack: UiEvent
    data class Navigate<T: Any>(val route: T): UiEvent
    data class ShowSnackbar(val message: String) : UiEvent
}