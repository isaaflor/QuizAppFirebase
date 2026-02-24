// --- GEMINI HEADER ---
package com.example.fourquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.model.Category
import com.example.quizapp.ui.UiEvent
import com.example.quizapp.ui.components.CategoryCard
import com.example.quizapp.ui.navigation.LeaderboardRoute
import com.example.quizapp.ui.navigation.LoginRoute
import com.example.quizapp.ui.navigation.QuizRoute
import com.example.quizapp.ui.screens.auth.AuthViewModel
import com.example.quizapp.ui.screens.home.HomeScreenEvent
import com.example.quizapp.ui.screens.home.HomeScreenViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    authViewModel: AuthViewModel,
    navigateToQuizScreen: (id: String) -> Unit,
    navigateToLeaderboardScreen: () -> Unit,
    navigateToLoginScreen: () -> Unit
) {
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when(uiEvent){
                is UiEvent.Navigate<*> -> {
                    when(uiEvent.route){
                        is QuizRoute -> { navigateToQuizScreen(uiEvent.route.id) }
                        is LeaderboardRoute -> { navigateToLeaderboardScreen() }
                        is LoginRoute -> {
                            authViewModel.signOut()
                            navigateToLoginScreen()
                        }
                    }
                }
                is UiEvent.ShowSnackbar -> {}
                is UiEvent.NavigateBack -> {}
            }
        }
    }

    HomeContent(
        categories = categories,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun HomeContent(
    categories: List<Category>,
    onEvent: (HomeScreenEvent) -> Unit
){
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Olá, <UserName>!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = {
                    onEvent(HomeScreenEvent.onViewLeaderboard)
                }
                , modifier = Modifier.fillMaxWidth(0.8f)) {
                Text("Ver Leaderboard")
            }

            LazyColumn() {
                itemsIndexed(categories){ index, category ->
                    CategoryCard(
                        category,
                        onClick = {
                            onEvent(HomeScreenEvent.onStartQuiz(category.id))
                        }
                    )
                    if (index < categories.lastIndex){
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(64.dp))

            TextButton(onClick = {
                onEvent(HomeScreenEvent.onLogout)
            }) {
                Text("Sair da Conta", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
