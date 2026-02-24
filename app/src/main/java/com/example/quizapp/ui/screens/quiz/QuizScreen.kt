// --- GEMINI HEADER ---
package com.example.fourquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.screens.quiz.QuizViewModel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onQuizFinished: () -> Unit
) {
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()

    var selectedOptionIndex by remember { mutableStateOf(-1) }
    var isAnswerEvaluated by remember { mutableStateOf(false) } // Controla o estado de feedback visual

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (questions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val currentQuestion = questions[currentIndex]

            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Pergunta ${currentIndex + 1} de ${questions.size}",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = currentQuestion.text, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(24.dp))

                currentQuestion.options.forEachIndexed { index, option ->
                    val isCorrect = index == currentQuestion.correctAnswerIndex
                    val isSelected = index == selectedOptionIndex

                    // Lógica de cores baseada na resposta
                    val itemColor = if (isAnswerEvaluated) {
                        when {
                            isCorrect -> Color(0xFF2E7D32) // Verde Escuro para a certa
                            isSelected && !isCorrect -> Color(0xFFC62828) // Vermelho para a errada
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = isSelected,
                                onClick = { if (!isAnswerEvaluated) selectedOptionIndex = index }
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { if (!isAnswerEvaluated) selectedOptionIndex = index },
                            enabled = !isAnswerEvaluated,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = itemColor,
                                unselectedColor = itemColor,
                                disabledSelectedColor = itemColor,
                                disabledUnselectedColor = itemColor
                            )
                        )
                        Text(text = option, color = itemColor, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (!isAnswerEvaluated) {
                            // Primeiro clique: trava e mostra cores
                            isAnswerEvaluated = true
                        } else {
                            // Segundo clique: processa os pontos e avança
                            viewModel.submitAnswer(selectedOptionIndex, onQuizFinished)
                            selectedOptionIndex = -1
                            isAnswerEvaluated = false
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = selectedOptionIndex != -1
                ) {
                    val buttonText = if (!isAnswerEvaluated) {
                        "Confirmar"
                    } else if (currentIndex == questions.size - 1) {
                        "Finalizar"
                    } else {
                        "Próxima"
                    }
                    Text(buttonText)
                }
            }
        }
    }
}
// --- GEMINI FOOTER ---