package com.example.quizapp.model.repository.implementation

import com.example.quizapp.model.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): AuthRepository {

    override fun onStart(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        }
        catch (e: Exception){
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            true
        }
        catch (e: Exception) {
            false
        }
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }
}