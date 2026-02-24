package com.example.quizapp.model.repository.implementation

import com.example.quizapp.model.Question
import com.example.quizapp.model.repository.QuestionRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): QuestionRepository {
    override fun getQuestionsByCategory(categoryId: String): Flow<List<Question>>  = callbackFlow {
        val collection = db.collection("questions").whereEqualTo("categoryId", categoryId)
        val listener = collection.addSnapshotListener { snapshot, error ->
            if(error != null){
                close(error)
                return@addSnapshotListener
            }

            if(snapshot != null){
                val items = snapshot.toObjects(Question::class.java)
                trySend(items).isSuccess
            }
        }
        awaitClose {
            listener.remove()
        }
    }
}


