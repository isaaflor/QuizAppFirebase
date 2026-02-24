// --- GEMINI HEADER ---
package com.example.quizapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fourquiz.ui.screens.*
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
            LoginScreen(viewModel = hiltViewModel())
        }

        composable<RegisterRoute> {

        }

        composable<HomeRoute>{
            HomeScreen(
                viewModel = hiltViewModel(),
                navigateToQuizScreen = { id: String -> navController.navigate(QuizRoute(id)) },
                navigateToLeaderboard = { navController.navigate(LeaderboardRoute) }
            )
        }

        composable<QuizRoute>{

        }

        composable<LeaderboardRoute> {

        }
    }
}
