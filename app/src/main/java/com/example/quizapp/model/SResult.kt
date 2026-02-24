package com.example.quizapp.model

data class SResult(
    val userId: String? = "",
    val score: Int = 0,
    val category: String = "",
    val timestamp: Long,
)
