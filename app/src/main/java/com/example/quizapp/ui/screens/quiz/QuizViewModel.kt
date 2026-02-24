package com.example.quizapp.ui.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: QuizRepository,
    private val firestoreService: FirestoreService,
    private val authService: FirebaseAuthService // Injetado para pegar os dados do usuário
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

        if (selectedIndex == currentQuestion.correctAnswerIndex) {
            _score.value += 100
        }

        if (_currentIndex.value < _questions.value.size - 1) {
            _currentIndex.value += 1
        } else {
            finishQuiz(onQuizFinished)
        }
    }

    private fun finishQuiz(onQuizFinished: () -> Unit) {
        viewModelScope.launch {
            val userId = authService.getCurrentUserId() ?: "anon_id"
            val userName = authService.getCurrentUserName()

            firestoreService.pushResult(
                userId = userId,
                userName = userName,
                score = _score.value,
                totalQuestions = _questions.value.size
            )
            onQuizFinished()
        }
    }
}