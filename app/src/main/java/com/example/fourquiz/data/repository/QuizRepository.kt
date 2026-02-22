package com.example.fourquiz.data.repository

import com.example.fourquiz.data.local.dao.QuizDao
import com.example.fourquiz.data.local.entity.Question
import com.example.fourquiz.data.network.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QuizRepository(
    private val quizDao: QuizDao,
    private val firestoreService: FirestoreService
) {
    /**
     * Offline-first logic using your specific DAO and Firestore Service
     */
    fun getQuestions(): Flow<List<Question>> = flow {
        // 1. Emit local cached data immediately
        val localData = quizDao.getAllQuestions()
        if (localData.isNotEmpty()) {
            emit(localData)
        }

        // 2. Fetch fresh data from Firestore
        try {
            val remoteDataRaw = firestoreService.fetchQuestions()

            if (remoteDataRaw.isNotEmpty()) {
                // Map the remote Map<String, Any> into your local Question entity
                val remoteQuestions = remoteDataRaw.mapIndexed { index, map ->
                    Question(
                        id = (map["id"] as? Long)?.toInt() ?: index, // Fallback to index if ID is missing
                        text = map["text"] as? String ?: "",
                        options = map["options"] as? List<String> ?: emptyList(),
                        correctAnswerIndex = (map["correctAnswerIndex"] as? Long)?.toInt() ?: 0
                    )
                }

                // 3. Save remote data to Room (Single Source of Truth)
                quizDao.clearQuestions() // Clear old cache to prevent duplicates
                quizDao.insertQuestions(remoteQuestions)

                // 4. Emit the freshly updated local data
                emit(quizDao.getAllQuestions())
            }
        } catch (e: Exception) {
            // If network fails, the user still sees the localData emitted in step 1
            e.printStackTrace()
        }
    }
}
