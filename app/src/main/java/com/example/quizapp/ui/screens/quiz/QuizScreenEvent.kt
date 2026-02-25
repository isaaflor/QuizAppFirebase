package com.example.quizapp.ui.screens.quiz

interface QuizScreenEvent {
    data class OnSubmitAnswer(val selectedIndex: Int) : QuizScreenEvent
    data class OnSelectIndexChange(val selectOptionIndex: Int) : QuizScreenEvent
    data class OnEvaluateAnswer(val isAnswerEvaluated: Boolean) : QuizScreenEvent
}