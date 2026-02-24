package com.example.quizapp.model.repository

import com.example.quizapp.model.Result
import kotlinx.coroutines.flow.Flow

interface ResultRepository {
    fun getAllResultsFromUser(): Flow<List<Result>>
    suspend fun insertResultFromUser(score: Int, totalQuestions: Int): Boolean
}