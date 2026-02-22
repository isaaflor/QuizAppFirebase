// --- GEMINI HEADER ---
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

    /**
     * Fetches the top scores for the Leaderboard.
     */
    suspend fun fetchLeaderboard(): List<Map<String, Any>> {
        return try {
            // Orders by highest score and limits to top 50
            val snapshot = db.collection("results")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(50)
                .get().await()
            snapshot.documents.mapNotNull { it.data }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * DEBUG ONLY: Adds sample questions to Firestore to prevent infinite loading.
     * Run this once if your database is empty.
     */
    suspend fun seedQuestions() {
        val questionsCollection = db.collection("questions")
        val snapshot = questionsCollection.get().await()

        // Só adiciona se o banco estiver vazio para não duplicar
        if (snapshot.isEmpty) {
            val sampleQuestions = listOf(
                hashMapOf(
                    "id" to 1,
                    "text" to "Qual a linguagem oficial para Android Nativo?",
                    "options" to listOf("Java", "Kotlin", "Python", "Dart"),
                    "correctAnswerIndex" to 1
                ),
                hashMapOf(
                    "id" to 2,
                    "text" to "Qual componente substitui a Activity no Compose?",
                    "options" to listOf("Fragment", "View", "Composable", "Nenhum"),
                    "correctAnswerIndex" to 2
                ),
                hashMapOf(
                    "id" to 3,
                    "text" to "O que significa ADB?",
                    "options" to listOf("Android Data Base", "Android Debug Bridge", "App Data Bundle", "Nenhuma"),
                    "correctAnswerIndex" to 1
                ),
                hashMapOf(
                    "id" to 4,
                    "text" to "Qual arquivo controla as permissões do App?",
                    "options" to listOf("build.gradle", "MainActivity.kt", "AndroidManifest.xml", "google-services.json"),
                    "correctAnswerIndex" to 2
                )
            )

            for (question in sampleQuestions) {
                questionsCollection.add(question).await()
            }
        }
    }
}
// --- GEMINI FOOTER ---