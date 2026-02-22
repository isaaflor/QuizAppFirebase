// --- GEMINI CODE BLOCK START ---
package com.example.fourquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onStartQuiz: () -> Unit, onViewLeaderboard: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome to FourQuiz!", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(48.dp))
            Button(onClick = onStartQuiz, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Start Quiz")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = onViewLeaderboard, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Leaderboard")
            }
        }
    }
}
// --- GEMINI CODE BLOCK END ---