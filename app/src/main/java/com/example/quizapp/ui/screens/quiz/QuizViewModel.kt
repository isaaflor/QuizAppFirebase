package com.example.quizapp.ui.screens.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.quizapp.model.repository.AuthRepository
import com.example.quizapp.model.repository.LeaderboardRepository
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
    private val authRepository: AuthRepository,
    private val resultRepository: ResultRepository,
    private val leaderboardRepository: LeaderboardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val quizRoute: QuizRoute = savedStateHandle.toRoute()
    private val categoryId = quizRoute.id
    val questions = questionRepository.getQuestionsByCategory(categoryId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _score = MutableStateFlow(0L)
    val score: StateFlow<Long> = _score.asStateFlow()

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
        val currentQuestionList = questions.value
        if(currentQuestionList.isEmpty()) return

        val currentIndex = _currentIndex.value
        val currentQuestion = currentQuestionList[currentIndex]

        if (selectedIndex == currentQuestion.correctAnswerIndex) {
            _score.value += 100
        }

        if (currentIndex < currentQuestionList.size - 1) {
            _currentIndex.value += 1
            selectedOptionIndex = -1
            isAnswerEvaluated = false
        } else {
            submitQuiz(categoryId, _score.value, currentQuestionList.size)
        }
    }

    private fun submitQuiz(category: String, score: Long, totalQuestions: Int) = viewModelScope.launch {
        val currentUser = authRepository.getCurrentUser()
        val currentUserId = currentUser?.uid
        val currentUserEmail = currentUser?.email

        val successAddResult = resultRepository.insertResultFromUser(category, score, totalQuestions)
        val successUpdateLeaderboard = leaderboardRepository.updateUserLeaderboardScore(currentUserId, currentUserEmail, score)

        if (successAddResult || successUpdateLeaderboard) {
            _uiSend.send(UiEvent.NavigateBack)
        }
    }
}