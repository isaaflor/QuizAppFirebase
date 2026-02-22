# --- GEMINI HEADER ---
package com.example.fourquiz.data.repository

import com.example.fourquiz.data.local.QuizDao
import com.example.fourquiz.data.remote.FirebaseQuizService
import com.example.fourquiz.domain.model.Quiz
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class QuizRepository(
    private val quizDao: QuizDao,
    private val firebaseService: FirebaseQuizService
) {
    /**
     * Offline-first strategy: Room acts as the Single Source of Truth.
     */
    fun getQuizzes(): Flow<List<Quiz>> = flow {
        // 1. Immediately emit cached local data
        val localData = quizDao.getAllQuizzes().first()
        if (localData.isNotEmpty()) {
            emit(localData)
        }

        // 2. Attempt to fetch fresh data from Firebase
        try {
            val remoteData = firebaseService.fetchQuizzes()
            if (remoteData.isNotEmpty()) {
                // 3. Save remote data to Room (SSOT)
                quizDao.insertQuizzes(remoteData)
                // 4. Emit the newly updated local data
                emit(quizDao.getAllQuizzes().first())
            }
        } catch (e: Exception) {
            // If the network request fails, the user still has the local data emitted in step 1.
            // You might want to log this exception or emit an error state depending on your UI needs.
        }
    }
}
# --- GEMINI FOOTER ---