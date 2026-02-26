package com.example.quizapp.model

import com.google.firebase.firestore.DocumentId

data class Question(
    @DocumentId val id: String = "",
    val text: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0,
    val categoryId: String = "",
    val difficulty: String = ""
)
