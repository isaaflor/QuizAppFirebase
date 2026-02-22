// --- GEMINI HEADER ---
package com.example.fourquiz.data.network

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Attempts to log in a user with email and password.
     * Returns true if successful, false otherwise.
     */
    suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Attempts to register a new user with email and password.
     * Firebase automatically logs the user in upon successful registration.
     */
    suspend fun register(email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Optional utility to get the currently logged-in user's ID
     */
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
// --- GEMINI FOOTER ---