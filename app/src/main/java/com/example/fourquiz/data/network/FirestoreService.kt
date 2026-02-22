package com.example.fourquiz.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Fetches questions from the "questions" collection.
     */
    suspend fun fetchQuestions(): List<Map<String, Any>> {
        return try {
            val snapshot = db.collection("questions").get().await()
            snapshot.documents.mapNotNull { it.data }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Pushes a completed quiz result to the "results" collection.
     */
    suspend fun pushResult(userId: String, score: Int, totalQuestions: Int): Boolean {
        return try {
            val resultData = hashMapOf(
                "userId" to userId,
                "score" to score,
                "totalQuestions" to totalQuestions,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("results").add(resultData).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}