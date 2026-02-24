package com.example.quizapp.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.tooling.parseSourceInformation
import androidx.credentials.GetPasswordOption
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    init {
        checkAuthStatus()
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