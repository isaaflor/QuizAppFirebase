// --- GEMINI CODE BLOCK START ---
package com.example.fourquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "FourQuiz", style = MaterialTheme.typography.displayMedium)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onLoginSuccess, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Mock Login")
            }
        }
    }
}
// --- GEMINI CODE BLOCK END ---