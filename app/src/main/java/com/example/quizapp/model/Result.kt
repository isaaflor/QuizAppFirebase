package com.example.quizapp.model

import com.google.firebase.firestore.DocumentId

data class Result(
    @DocumentId val id: String = "",
    val userId: String = "",
    val score: Int = 0,
    val category: String = "",
    val timestamp: Long,
)
