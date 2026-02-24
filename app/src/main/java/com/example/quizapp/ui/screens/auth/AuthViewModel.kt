package com.example.quizapp.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.tooling.parseSourceInformation
import androidx.credentials.GetPasswordOption
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.repository.AuthRepository
import com.example.quizapp.ui.UiEvent
import com.example.quizapp.ui.screens.auth.login.LoginScreenEvent
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
        checkAuthStatus()
    }

    fun onEvent(event: LoginScreenEvent){
        when(event){
            is LoginScreenEvent.onEmailChange -> {
                this.email = event.email
            }

            is LoginScreenEvent.onPasswordChange -> {
                this.password = event.password
            }

            is LoginScreenEvent.onSignIn -> {

                if(email.isBlank()){
                    sendUiEvent(UiEvent.ShowSnackbar("Insira um email válido!"))
                    return
                }

                if(password.isBlank()){
                    sendUiEvent(UiEvent.ShowSnackbar("Insira uma senha válida!"))
                    return
                }

                login(email, password)
                if(!(_authState.value)){
                    sendUiEvent(UiEvent.ShowSnackbar("Erro ao realizar login! Verifique email e senha!"))
                    return
                }
            }
        }
    }

    fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
    fun checkAuthStatus(){
        firebaseUser.value = authRepository.onStart()
        _authState.value = firebaseUser.value != null
    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            _authState.value = authRepository.signIn(email, password)
        }
    }

    fun register(email: String, password: String){
        viewModelScope.launch {
            _authState.value = authRepository.signUp(email, password)
        }
    }

    fun signOut(){
        authRepository.signOut()
        _authState.value = false
    }
}