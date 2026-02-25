package com.example.quizapp.ui.screens.auth

interface AuthScreensEvent {
    data class onEmailChange(val email: String): AuthScreensEvent
    data class onPasswordChange(val password: String): AuthScreensEvent
    data object onSignIn: AuthScreensEvent
    data object onSignUp: AuthScreensEvent
    data object onSignInClick: AuthScreensEvent
    data object onSignUpClick: AuthScreensEvent
    data class onGoogleSignIn(val idToken: String): AuthScreensEvent
}