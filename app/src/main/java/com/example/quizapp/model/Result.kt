package com.example.quizapp.model

import com.google.firebase.firestore.DocumentId

data class Result(
    @DocumentId val id: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val quizCategory: String = "",
    val score: Long = 0,
    val totalQuestions: Int = 0,
)
