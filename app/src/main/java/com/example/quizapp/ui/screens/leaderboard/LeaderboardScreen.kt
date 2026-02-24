package com.example.quizapp.ui.screens.leaderboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.model.Result
import com.example.quizapp.ui.UiEvent

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel,
    navigateBack: () -> Unit
) {
    val results = viewModel.results.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when(uiEvent){
                is UiEvent.Navigate<*> -> {}
                is UiEvent.ShowSnackbar -> {}
                is UiEvent.NavigateBack -> {
                    navigateBack()
                }
            }
        }
    }

    LeaderboardContent(
        results = results as List<Result>,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun LeaderboardContent(
    results: List<Result>,
    onEvent: (LeaderboardScreenEvent) -> Unit
){
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(
                text = "Leaderboard Global",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                if (results.isEmpty()) {
                    item { Text("Nenhum resultado ainda. Seja o primeiro!") }
                } else {
                    itemsIndexed(results) { index, result ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = result.userId, style = MaterialTheme.typography.bodyLarge)
                            Text(text = "${result.score} pts", style = MaterialTheme.typography.bodyLarge)
                        }
                        HorizontalDivider()
                    }
                }
            }

            Button(
                onClick = { onEvent(LeaderboardScreenEvent.onExitLeaderboard) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            ) {
                Text("Voltar para Home")
            }
        }
    }
}