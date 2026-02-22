// --- GEMINI HEADER ---
package com.example.fourquiz.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fourquiz.data.local.entity.Question
import com.example.fourquiz.data.network.FirestoreService
import com.example.fourquiz.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: QuizRepository,
    private val firestoreService: FirestoreService
) : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _score = MutableStateFlow(0)

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            repository.getQuestions().collect { questionList ->
                _questions.value = questionList
            }
        }
    }

    fun submitAnswer(selectedIndex: Int, onQuizFinished: () -> Unit) {
        val currentQuestion = _questions.value[_currentIndex.value]

        // Checa se acertou e soma os pontos
        if (selectedIndex == currentQuestion.correctAnswerIndex) {
            _score.value += 100 // Exemplo: 100 pontos por acerto
        }

        // Vai para a próxima pergunta ou finaliza o quiz
        if (_currentIndex.value < _questions.value.size - 1) {
            _currentIndex.value += 1
        } else {
            finishQuiz(onQuizFinished)
        }
    }

    private fun finishQuiz(onQuizFinished: () -> Unit) {
        viewModelScope.launch {
            // TODO: Pegar o userId real do FirebaseAuthService depois
            val mockUserId = "user_123"
            firestoreService.pushResult(
                userId = mockUserId,
                score = _score.value,
                totalQuestions = _questions.value.size
            )
            onQuizFinished()
        }
    }
}
// --- GEMINI FOOTER ---