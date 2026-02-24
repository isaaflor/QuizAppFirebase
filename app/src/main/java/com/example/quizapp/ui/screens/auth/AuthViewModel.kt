package com.example.quizapp.ui.screens.auth

import com.example.quizapp.model.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) {

    fun onEvent(){
        
    }
}