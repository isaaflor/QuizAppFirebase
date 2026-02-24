package com.example.quizapp.model.repository

import com.example.quizapp.model.Question

interface QuestionRepository{
    suspend fun getQuestionsByCategory(categoryId: String): List<Question>
}
