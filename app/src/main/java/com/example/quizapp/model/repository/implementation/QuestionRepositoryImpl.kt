package com.example.quizapp.model.repository.implementation

import com.example.quizapp.model.Question
import com.example.quizapp.model.repository.QuestionRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): QuestionRepository {
    override suspend fun getQuestionsByCategory(categoryId: String): List<Question> {
        return try {
            val questions = db.collection("questions").whereEqualTo("categoryId", categoryId).get().await()
            questions.toObjects(Question::class.java)
        }
        catch (e: Exception){
            emptyList()
        }
    }
}


