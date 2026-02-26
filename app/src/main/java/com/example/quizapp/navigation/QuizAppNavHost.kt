package com.example.quizapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.ui.screens.auth.AuthViewModel
import com.example.quizapp.ui.screens.auth.login.LoginScreen
import com.example.quizapp.ui.screens.auth.register.RegisterScreen
import com.example.quizapp.ui.screens.history.HistoryScreen
import com.example.quizapp.ui.screens.home.HomeScreen
import com.example.quizapp.ui.screens.leaderboard.LeaderboardScreen
import com.example.quizapp.ui.screens.quiz.QuizScreen
import kotlinx.serialization.Serializable
import javax.inject.Inject

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

@Serializable
object HistoryRoute


@Composable
fun QuizAppNavHost(auth: AuthViewModel = hiltViewModel()){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination =
        if(auth.authState.value){ HomeRoute }
        else { LoginRoute }
    ) {

        composable<LoginRoute> {
            LoginScreen(
                viewModel = hiltViewModel(),
                navigateToHomeScreen = { navController.navigate(HomeRoute) },
                navigateToRegisterScreen = { navController.navigate(RegisterRoute) }
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                viewModel = hiltViewModel(),
                navigateToHomeScreen = { navController.navigate(HomeRoute) },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<HomeRoute>{
            HomeScreen(
                viewModel = hiltViewModel(),
                authViewModel = hiltViewModel(),
                navigateToQuizScreen = { id: String -> navController.navigate(QuizRoute(id)) },
                navigateToLeaderboardScreen = { navController.navigate(LeaderboardRoute) },
                navigateToHistoryScreen = { navController.navigate(HistoryRoute) },
                navigateToLoginScreen = { navController.navigate(LoginRoute) },
            )
        }

        composable<QuizRoute>{
            QuizScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<LeaderboardRoute> {
            LeaderboardScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<HistoryRoute> {
            HistoryScreen(
                viewModel = hiltViewModel(),
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}

