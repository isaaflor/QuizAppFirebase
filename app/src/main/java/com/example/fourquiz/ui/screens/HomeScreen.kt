// --- GEMINI HEADER ---
package com.example.fourquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fourquiz.FourQuizApplication

@Composable
fun HomeScreen(
    onStartQuiz: () -> Unit,
    onViewLeaderboard: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val authService = (context.applicationContext as FourQuizApplication).container.firebaseAuthService
    val userName = authService.getCurrentUserName()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Olá, $userName!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onStartQuiz, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Iniciar Quiz")
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(onClick = onViewLeaderboard, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Ver Leaderboard")
            }

            Spacer(modifier = Modifier.height(64.dp))

            TextButton(onClick = {
                authService.logout()
                onLogout()
            }) {
                Text("Sair da Conta", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
// --- GEMINI FOOTER ---