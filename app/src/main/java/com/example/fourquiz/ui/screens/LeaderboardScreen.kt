// --- GEMINI CODE BLOCK START ---
package com.example.fourquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class PlayerScore(val name: String, val score: Int)

@Composable
fun LeaderboardScreen(onBackToHome: () -> Unit) {
    // Mock Data
    val mockScores = listOf(
        PlayerScore("Alice", 1500),
        PlayerScore("Bob", 1200),
        PlayerScore("Charlie", 950),
        PlayerScore("Diana", 800)
    )

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Leaderboard",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(mockScores) { player ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = player.name, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "${player.score} pts", style = MaterialTheme.typography.bodyLarge)
                    }
                    Divider()
                }
            }

            Button(
                onClick = onBackToHome,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
            ) {
                Text("Back to Home")
            }
        }
    }
}
// --- GEMINI CODE BLOCK END ---