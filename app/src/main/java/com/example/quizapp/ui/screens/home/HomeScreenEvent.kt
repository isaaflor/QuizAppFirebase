package com.example.quizapp.ui.screens.home

interface HomeScreenEvent {
    data class onStartQuiz(val id: String): HomeScreenEvent
    data object onViewLeaderboard: HomeScreenEvent
    data object onViewHistory: HomeScreenEvent
    data object onLogout: HomeScreenEvent
}