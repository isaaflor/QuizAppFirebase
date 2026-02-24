package com.example.quizapp.model.repository

import com.example.quizapp.model.Result
import kotlinx.coroutines.flow.Flow

interface ResultRepository {
    fun getAllResultsFromUser(): Flow<List<Result>>
    suspend fun insertResultFromUser(score: Int, category: String, timestamp: Long): Boolean
}