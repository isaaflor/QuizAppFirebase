// --- GEMINI HEADER ---
package com.example.fourquiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizScreen(
    viewModel: QuizViewModel, // Injetando o ViewModel
    onQuizFinished: () -> Unit
) {
    // Observando os estados do ViewModel
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()

    var selectedOptionIndex by remember { mutableStateOf(-1) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (questions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Mostra loading enquanto busca do Room/Firestore
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
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (index == selectedOptionIndex),
                                onClick = { selectedOptionIndex = index }
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (index == selectedOptionIndex),
                            onClick = { selectedOptionIndex = index }
                        )
                        Text(text = option, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        viewModel.submitAnswer(selectedOptionIndex, onQuizFinished)
                        selectedOptionIndex = -1 // Reseta a seleção para a próxima pergunta
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = selectedOptionIndex != -1
                ) {
                    val buttonText = if (currentIndex == questions.size - 1) "Finalizar" else "Próxima"
                    Text(buttonText)
                }
            }
        }
    }
}
// --- GEMINI FOOTER ---