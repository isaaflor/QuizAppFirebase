package com.example.quizapp.model.repository

interface AuthRepository {
    fun onStart(): Boolean
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Boolean
    fun signOut()
}