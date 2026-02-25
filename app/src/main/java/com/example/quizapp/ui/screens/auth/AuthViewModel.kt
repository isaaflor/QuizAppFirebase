package com.example.quizapp.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.repository.AuthRepository
import com.example.quizapp.ui.UiEvent
import com.example.quizapp.model.navigation.HomeRoute
import com.example.quizapp.model.navigation.RegisterRoute
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _authState = MutableStateFlow<Boolean>(false)
    val authState: StateFlow<Boolean> = _authState

    private val firebaseUser = MutableStateFlow<FirebaseUser?>(null)

    var email by mutableStateOf<String>("")
        private set

    var password by mutableStateOf<String>("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        firebaseUser.value = authRepository.getCurrentUser()
        _authState.value = firebaseUser.value != null
    }

    fun onEvent(event: AuthScreensEvent) {
        when (event) {
            is AuthScreensEvent.onEmailChange -> {
                this.email = event.email
            }

            is AuthScreensEvent.onPasswordChange -> {
                this.password = event.password
            }

            is AuthScreensEvent.onSignIn -> {

                if (email.isBlank()) {
                    sendUiEvent(UiEvent.ShowSnackbar("Insira um email válido!"))
                    return
                }

                if (password.isBlank()) {
                    sendUiEvent(UiEvent.ShowSnackbar("Insira uma senha válida!"))
                    return
                }

                login(email, password)
            }

            is AuthScreensEvent.onSignUp -> {
                if (email.isBlank()) {
                    sendUiEvent(UiEvent.ShowSnackbar("Insira um email válido!"))
                    return
                }

                if (password.isBlank()) {
                    sendUiEvent(UiEvent.ShowSnackbar("Insira uma senha válida!"))
                    return
                }

                register(email, password)
            }

            is AuthScreensEvent.onSignUpClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(RegisterRoute))
                }
            }

            is AuthScreensEvent.onSignInClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateBack)
                }
            }
        }
    }

    fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        val success = authRepository.signIn(email, password)
        _authState.value = success
        if (success) {
            _uiEvent.send(UiEvent.Navigate(HomeRoute))
        } else {
            _uiEvent.send(UiEvent.ShowSnackbar("Erro ao realizar login! Verifique email e senha!"))
        }
    }


    fun register(email: String, password: String) = viewModelScope.launch {
        val success = authRepository.signUp(email, password)
        _authState.value = success
        if (success) {
            _uiEvent.send(UiEvent.Navigate(HomeRoute))
        } else {
            _uiEvent.send(UiEvent.ShowSnackbar("Erro ao realizar cadastro! Verifique email e senha!"))
        }
    }

    fun signOut() {
        authRepository.signOut()
        _authState.value = false
    }
}
