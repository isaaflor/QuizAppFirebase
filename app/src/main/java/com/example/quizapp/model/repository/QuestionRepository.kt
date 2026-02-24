package com.example.quizapp.model.repository

import com.example.quizapp.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository{
    fun getQuestionsByCategory(categoryId: String): Flow<List<Question>>
}
