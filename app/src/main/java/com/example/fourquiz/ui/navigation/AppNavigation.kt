// --- GEMINI CODE BLOCK START ---
package com.example.fourquiz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fourquiz.ui.screens.HomeScreen
import com.example.fourquiz.ui.screens.LeaderboardScreen
import com.example.fourquiz.ui.screens.LoginScreen
import com.example.fourquiz.ui.screens.QuizScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true } // Prevents going back to login
                }
            })
        }
        composable("home") {
            HomeScreen(
                onStartQuiz = { navController.navigate("quiz") },
                onViewLeaderboard = { navController.navigate("leaderboard") }
            )
        }
        composable("quiz") {
            QuizScreen(onQuizFinished = { navController.navigate("leaderboard") })
        }
        composable("leaderboard") {
            LeaderboardScreen(onBackToHome = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            })
        }
    }
}
// --- GEMINI CODE BLOCK END ---