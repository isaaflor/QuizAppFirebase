package com.example.quizapp.ui.screens.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.quizapp.model.repository.QuestionRepository
import com.example.quizapp.model.repository.ResultRepository
import com.example.quizapp.ui.UiEvent
import com.example.quizapp.navigation.QuizRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val resultRepository: ResultRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val quizRoute: QuizRoute = savedStateHandle.toRoute()
    private val category = quizRoute.id
    val questions = questionRepository.getQuestionsByCategory(category).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    var selectedOptionIndex by mutableStateOf<Int>(-1)
        private set

    var isAnswerEvaluated by mutableStateOf<Boolean>(false)
        private set

    private val _uiSend = Channel<UiEvent>()
    val uiSend = _uiSend.receiveAsFlow()



    fun onEvent(event: QuizScreenEvent){
        when(event){
            is QuizScreenEvent.OnSubmitAnswer -> {
                submitAnswer(event.selectedIndex)
            }
            is QuizScreenEvent.OnSelectIndexChange -> {
                selectedOptionIndex = event.selectOptionIndex
            }
            is QuizScreenEvent.OnEvaluateAnswer -> {
                isAnswerEvaluated = event.isAnswerEvaluated
            }
        }
    }
    fun submitAnswer(selectedIndex: Int) {
        val currentQuestion = questions.value[_currentIndex.value]

        if(selectedIndex == -1){
            submitQuiz(currentQuestion.categoryId, _score.value, questions.value.size)
        }

        if (selectedIndex == currentQuestion.correctAnswerIndex) {
            _score.value += 100
        }

        if (_currentIndex.value < questions.value.size - 1) {
            _currentIndex.value += 1
        } else {
            submitQuiz(currentQuestion.categoryId, _score.value, questions.value.size)
        }
    }

    private fun submitQuiz(category: String, score: Int, totalQuestions: Int) = viewModelScope.launch {
        val success = resultRepository.insertResultFromUser(category, score, totalQuestions)
        if (success) {
            _uiSend.send(UiEvent.NavigateBack)
        }
    }
}