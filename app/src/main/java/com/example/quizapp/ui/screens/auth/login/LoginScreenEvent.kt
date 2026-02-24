package com.example.quizapp.ui.screens.auth.login


interface LoginScreenEvent {
    data class onEmailChange(val email: String): LoginScreenEvent
    data class onPasswordChange(val password: String): LoginScreenEvent
    data object onSignIn: LoginScreenEvent
    data object onSignUpClick: LoginScreenEvent
}