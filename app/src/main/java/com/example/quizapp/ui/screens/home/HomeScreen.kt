package com.example.quizapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Leaderboard // Requer material-icons-extended
import androidx.compose.material.icons.filled.Quiz        // Requer material-icons-extended
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quizapp.model.Category
import com.example.quizapp.navigation.HistoryRoute
import com.example.quizapp.ui.UiEvent
import com.example.quizapp.navigation.LeaderboardRoute
import com.example.quizapp.navigation.LoginRoute
import com.example.quizapp.navigation.QuizRoute
import com.example.quizapp.ui.screens.auth.AuthViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    authViewModel: AuthViewModel,
    navigateToQuizScreen: (id: String) -> Unit,
    navigateToLeaderboardScreen: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
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
                        is HistoryRoute -> { navigateToHistoryScreen() }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    categories: List<Category>,
    onEvent: (HomeScreenEvent) -> Unit
){
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("Bem-vindo!", style = MaterialTheme.typography.bodyMedium)
                        Text("Escolha um Quiz", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(HomeScreenEvent.onViewLeaderboard) }) {
                        Icon(Icons.Default.Leaderboard, contentDescription = "Placar")
                    }
                    IconButton(onClick = { onEvent(HomeScreenEvent.onViewHistory) }) {
                        Icon(Icons.Default.History, contentDescription = "Histórico")
                    }
                    IconButton(onClick = { onEvent(HomeScreenEvent.onLogout) }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair")
                    }
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
                    category = category.id,
                    onClick = { onEvent(HomeScreenEvent.onStartQuiz(category.id)) }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ícone circular decorativo
            Surface(
                modifier = Modifier.size(56.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
