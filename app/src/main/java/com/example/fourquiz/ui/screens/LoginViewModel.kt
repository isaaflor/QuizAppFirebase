// --- GEMINI HEADER ---
package com.example.fourquiz.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fourquiz.data.network.FirebaseAuthService
import kotlinx.coroutines.launch

class LoginViewModel(private val authService: FirebaseAuthService) : ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun login(onSuccess: () -> Unit) {
        if (email.value.isBlank() || password.value.isBlank()) {
            errorMessage.value = "Preenche todos os campos."
            return
        }
        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            val success = authService.login(email.value, password.value)
            isLoading.value = false
            if (success) {
                onSuccess()
            } else {
                errorMessage.value = "Falha no login. Verifica os dados ou a tua ligação."
            }
        }
    }

    fun register(onSuccess: () -> Unit) {
        if (email.value.isBlank() || password.value.isBlank()) {
            errorMessage.value = "Preenche todos os campos para criar conta."
            return
        }
        if (password.value.length < 6) {
            errorMessage.value = "A palavra-passe deve ter pelo menos 6 caracteres."
            return
        }

        isLoading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            val success = authService.register(email.value, password.value)
            isLoading.value = false
            if (success) {
                // O Firebase faz o login automaticamente após o registo com sucesso
                onSuccess()
            } else {
                errorMessage.value = "Falha ao criar conta. O e-mail pode ser inválido ou já estar em uso."
            }
        }
    }
}
// --- GEMINI FOOTER ---