package com.example.quizapp.ui.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.repository.QuestionRepository
import com.example.quizapp.model.repository.ResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val resultRepository: ResultRepository,
    private val category: String
) : ViewModel() {

    val questions = questionRepository.getQuestionsByCategory(category).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()
    private val _score = MutableStateFlow(0)

    fun submitAnswer(selectedIndex: Int, onQuizFinished: () -> Unit) {
        val currentQuestion = questions.value[_currentIndex.value]

        if (selectedIndex == currentQuestion.correctAnswerIndex) {
            _score.value += 100
        }

        if (_currentIndex.value < questions.value.size - 1) {
            _currentIndex.value += 1
        } else {
            finishQuiz(onQuizFinished)
        }
    }

    private fun finishQuiz(onQuizFinished: () -> Unit) {
        viewModelScope.launch {
            val score = _score.value
            val totalQuestions = questions.value.size
            resultRepository.insertResultFromUser(score, totalQuestions)
            onQuizFinished()
        }
    }
}