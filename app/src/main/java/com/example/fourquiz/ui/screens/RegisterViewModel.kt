// --- GEMINI HEADER ---
package com.example.fourquiz.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fourquiz.data.network.FirebaseAuthService
import kotlinx.coroutines.launch

class RegisterViewModel(private val authService: FirebaseAuthService) : ViewModel() {
    var name = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun register(onSuccess: () -> Unit) {
        if (name.value.isBlank() || email.value.isBlank() || password.value.isBlank()) {
            errorMessage.value = "Preencha todos os campos."
            return
        }
        if (password.value.length < 6) {
            errorMessage.value = "A senha deve ter pelo menos 6 caracteres."
            return
        }

        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            val success = authService.register(email.value, password.value, name.value)
            isLoading.value = false
            if (success) onSuccess() else errorMessage.value = "Erro ao criar conta. Email pode já estar em uso."
        }
    }
}
// --- GEMINI FOOTER ---