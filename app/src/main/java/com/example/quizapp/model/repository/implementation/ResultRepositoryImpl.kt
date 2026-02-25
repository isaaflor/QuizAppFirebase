package com.example.quizapp.model.repository.implementation

import com.example.quizapp.model.Result
import com.example.quizapp.model.SResult
import com.example.quizapp.model.repository.ResultRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

    private val currentUser: FirebaseUser?
        get() = auth.currentUser

    override fun getAllResultsFromUser(): Flow<List<Result>> = callbackFlow {
        val uid = currentUser?.uid
        if (uid == null){
            trySend(emptyList())
            return@callbackFlow
        }

        val collections = db.collection("results").whereEqualTo("userId", uid)
        val listener = collections.addSnapshotListener { snapshot, error ->
            if(error != null){
                close(error)
                return@addSnapshotListener
            }
            snapshot?.let {
                val items = it.toObjects(Result::class.java)
                trySend(items)
            }
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun insertResultFromUser(quizCategory: String, score: Long, totalQuestions: Int): Boolean {
        return try {
            val user = currentUser ?: return false

            val result = SResult(
                userId = user.uid,
                userEmail = user.email,
                quizCategory = quizCategory,
                score = score,
                totalQuestions = totalQuestions
            )
            db.collection("results").add(result).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}