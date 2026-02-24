package com.example.quizapp.model

import com.google.firebase.firestore.DocumentId

data class Category(
    @DocumentId val id: String = "",
    val name: String = "",
    val description: String = "",
    val time: Long = 0
)
