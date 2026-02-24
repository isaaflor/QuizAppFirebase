package com.example.quizapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fourquiz.ui.screens.*
import com.example.quizapp.ui.screens.auth.login.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
object HomeRoute

@Serializable
data class QuizRoute(val id: String)

@Serializable
object LeaderboardRoute

@Composable
fun QuizAppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {

        composable<LoginRoute> {
            LoginScreen(
                viewModel = hiltViewModel(),
                navigateToHomeScreen = { navController.navigate(HomeRoute) },
                navigateToRegisterScreen = { navController.navigate(RegisterRoute) }
            )
        }

        composable<RegisterRoute> {

        }

        composable<HomeRoute>{
            HomeScreen(
                viewModel = hiltViewModel(),
                authViewModel = hiltViewModel(),
                navigateToQuizScreen = { id: String -> navController.navigate(QuizRoute(id)) },
                navigateToLeaderboardScreen = { navController.navigate(LeaderboardRoute) },
                navigateToLoginScreen = { navController.navigate(LoginRoute) },
            )
        }

        composable<QuizRoute>{

        }

        composable<LeaderboardRoute> {

        }
    }
}
