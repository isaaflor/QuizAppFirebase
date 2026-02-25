package com.example.quizapp.ui.screens.quiz

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.model.Question
import com.example.quizapp.ui.UiEvent

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    navigateBack: () -> Unit
) {
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val score by viewModel.score.collectAsState()
    val selectedOptionIndex = viewModel.selectedOptionIndex
    val isAnswerEvaluated = viewModel.isAnswerEvaluated


    LaunchedEffect(Unit) {
        viewModel.uiSend.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Navigate<*> -> {}
                is UiEvent.ShowSnackbar -> {}
                is UiEvent.NavigateBack -> { navigateBack() }
            }
        }
    }

    QuestionContent(
        onEvent = viewModel::onEvent,
        questions = questions,
        score = score,
        currentIndex = currentIndex,
        selectedOptionIndex = selectedOptionIndex,
        isAnswerEvaluated = isAnswerEvaluated
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionContent(
    onEvent: (QuizScreenEvent) -> Unit,
    questions: List<Question>,
    score: Int,
    currentIndex: Int,
    selectedOptionIndex: Int,
    isAnswerEvaluated: Boolean,
){
    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val currentQuestion = questions[currentIndex]
        val progress = (currentIndex + 1).toFloat() / questions.size

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Questão ${currentIndex + 1} de ${questions.size}", style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        IconButton(onClick = { onEvent(QuizScreenEvent.OnSubmitAnswer(-1)) }) {
                            Icon(Icons.Default.Close, contentDescription = "Sair")
                        }
                    },
                    actions = {
                        Text(
                            "${score} pts",
                            modifier = Modifier.padding(end = 16.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Barra de Progresso
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Card da Pergunta
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = currentQuestion.text,
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Lista de Opções
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(currentQuestion.options) { index, option ->
                        OptionItem(
                            option = option,
                            isSelected = selectedOptionIndex == index,
                            isCorrect = index == currentQuestion.correctAnswerIndex,
                            isEvaluated = isAnswerEvaluated,
                            onClick = {
                                if (!isAnswerEvaluated) {
                                    onEvent(QuizScreenEvent.OnSelectIndexChange(index))
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botão de Ação
                Button(
                    onClick = {
                        if (!isAnswerEvaluated) {
                            onEvent(QuizScreenEvent.OnEvaluateAnswer(true))
                        } else {
                            onEvent(QuizScreenEvent.OnSubmitAnswer(selectedOptionIndex))
                            onEvent(QuizScreenEvent.OnSelectIndexChange(-1))
                            onEvent(QuizScreenEvent.OnEvaluateAnswer(false))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    enabled = selectedOptionIndex != -1
                ) {
                    val buttonText = if (!isAnswerEvaluated) "Confirmar"
                    else if (currentIndex == questions.size - 1) "Finalizar"
                    else "Próxima Pergunta"
                    Text(buttonText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun OptionItem(
    option: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isEvaluated: Boolean,
    onClick: () -> Unit
) {
    // Lógica de cores baseada no estado da avaliação
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isEvaluated && isCorrect -> Color(0xFFC8E6C9) // Verde claro se correto
            isEvaluated && isSelected && !isCorrect -> Color(0xFFFFCDD2) // Vermelho claro se erro
            isSelected -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.surface
        }, label = ""
    )

    val borderColor = when {
        isEvaluated && isCorrect -> Color(0xFF4CAF50)
        isEvaluated && isSelected && !isCorrect -> Color(0xFFF44336)
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
            .clickable(enabled = !isEvaluated) { onClick() },
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = option,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )

            if (isEvaluated && isCorrect) {
                Text("✓", color = Color(0xFF2E7D32), fontWeight = FontWeight.Black)
            } else if (isEvaluated && isSelected && !isCorrect) {
                Text("✕", color = Color(0xFFC62828), fontWeight = FontWeight.Black)
            }
        }
    }
}