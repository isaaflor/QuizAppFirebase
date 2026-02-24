package com.example.quizapp.model.repository.implementation

import com.example.quizapp.model.Result
import com.example.quizapp.model.SResult
import com.example.quizapp.model.repository.ResultRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ResultRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
): ResultRepository {

    private val userId = auth.currentUser?.uid

    override fun getAllResultsFromUser(): Flow<List<Result>> = callbackFlow {
        val collections = db.collection("results").whereEqualTo("userId", userId)
        val listener = collections.addSnapshotListener { snapshot, error ->
            if(error != null){
                close(error)
                return@addSnapshotListener
            }

            if(snapshot != null){
                val items = snapshot.toObjects(Result::class.java)
                trySend(items)
            }
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun insertResultFromUser(score: Int, category: String, timestamp: Long): Boolean {
            val result = SResult(
                userId = this.userId,
                score = score,
                category = category,
                timestamp = timestamp
            )
            db.collection("results").add(result).await()
            return true
    }
}