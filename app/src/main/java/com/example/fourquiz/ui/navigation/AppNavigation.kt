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
import com.example.fourquiz.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
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
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return RegisterViewModel(appContainer.firebaseAuthService) as T
                    }
                }
            )

            RegisterScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            HomeScreen(
                onStartQuiz = { navController.navigate("quiz") },
                onViewLeaderboard = { navController.navigate("leaderboard") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("quiz") {
            val quizViewModel: QuizViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return QuizViewModel(
                            appContainer.quizRepository,
                            appContainer.firestoreService,
                            appContainer.firebaseAuthService // Adicionado aqui!
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