package com.example.quizapp.model.repository

import com.example.quizapp.model.Leaderboard
import kotlinx.coroutines.flow.Flow

interface LeaderboardRepository {
    fun getAllLeaderboardEntries(): Flow<List<Leaderboard>>
    suspend fun updateUserLeaderboardScore(userId: String?, userEmail: String?, score: Long): Boolean
}