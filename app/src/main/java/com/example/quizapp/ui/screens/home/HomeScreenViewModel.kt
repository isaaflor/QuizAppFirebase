package com.example.quizapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.repository.AuthRepository
import com.example.quizapp.model.repository.CategoryRepository
import com.example.quizapp.ui.UiEvent
import com.example.quizapp.ui.navigation.LeaderboardRoute
import com.example.quizapp.ui.navigation.LoginRoute
import com.example.quizapp.ui.navigation.QuizRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val categoryRepository: CategoryRepository
): ViewModel(){

    val categories = categoryRepository.getAllCategories().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: HomeScreenEvent){
        when(event){
            is HomeScreenEvent.onStartQuiz -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(QuizRoute(event.id)))
                }
            }

            is HomeScreenEvent.onViewLeaderboard ->{
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(LeaderboardRoute))
                }
            }

            is HomeScreenEvent.onLogout -> {
                authRepository.signOut()
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(LoginRoute))
                }
            }
        }
    }
}