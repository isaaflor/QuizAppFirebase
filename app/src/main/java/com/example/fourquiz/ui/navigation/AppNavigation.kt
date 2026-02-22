// --- GEMINI HEADER ---
package com.example.fourquiz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fourquiz.FourQuizApplication
import com.example.fourquiz.ui.screens.HomeScreen
import com.example.fourquiz.ui.screens.LeaderboardScreen
import com.example.fourquiz.ui.screens.LoginScreen
import com.example.fourquiz.ui.screens.QuizScreen
import com.example.fourquiz.ui.screens.QuizViewModel
import com.example.fourquiz.ui.screens.LoginViewModel
import com.example.fourquiz.ui.screens.LeaderboardViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Grab the application context to access our manual Dependency Injection container
    val context = LocalContext.current
    val appContainer = (context.applicationContext as FourQuizApplication).container

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            val loginViewModel: LoginViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return LoginViewModel(appContainer.firebaseAuthService) as T
                    }
                }
            )

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onStartQuiz = { navController.navigate("quiz") },
                onViewLeaderboard = { navController.navigate("leaderboard") }
            )
        }

        composable("quiz") {
            val quizViewModel: QuizViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return QuizViewModel(
                            appContainer.quizRepository,
                            appContainer.firestoreService
                        ) as T
                    }
                }
            )

            QuizScreen(
                viewModel = quizViewModel,
                onQuizFinished = { navController.navigate("leaderboard") }
            )
        }

        composable("leaderboard") {
            val leaderboardViewModel: LeaderboardViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return LeaderboardViewModel(appContainer.firestoreService) as T
                    }
                }
            )

            LeaderboardScreen(
                viewModel = leaderboardViewModel,
                onBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
// --- GEMINI FOOTER ---