package com.example.quizapp.model

import com.google.firebase.firestore.DocumentId

data class Leaderboard(
    @DocumentId val id: String = "",
    val userId: String? = null,
    val userEmail: String? = null,
    var totalScore: Long = 0
)
