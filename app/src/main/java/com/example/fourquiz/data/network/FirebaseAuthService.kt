// --- GEMINI HEADER ---
package com.example.fourquiz.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class FirebaseAuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Agora recebemos o nome e salvamos no perfil do usuário!
    suspend fun register(email: String, password: String, name: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            user?.updateProfile(profileUpdates)?.await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // Pega o nome que salvamos no registro (ou "Jogador" se falhar)
    fun getCurrentUserName(): String = auth.currentUser?.displayName ?: "Jogador"
}
// --- GEMINI FOOTER ---V