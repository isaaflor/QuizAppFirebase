package com.example.quizapp.model

data class SResult(
    val userId: String? = "",
    val userEmail: String? = "",
    val quizCategory: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
)
