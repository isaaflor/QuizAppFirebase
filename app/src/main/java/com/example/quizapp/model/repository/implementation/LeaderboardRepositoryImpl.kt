package com.example.quizapp.model.repository.implementation

import com.example.quizapp.model.Leaderboard
import com.example.quizapp.model.repository.LeaderboardRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LeaderboardRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): LeaderboardRepository{

    override fun getAllLeaderboardEntries(): Flow<List<Leaderboard>> = callbackFlow{

        val query = db.collection("leaderboard")
            .orderBy("totalScore", com.google.firebase.firestore.Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val leaderboard = snapshot?.documents?.mapNotNull {doc ->
                    doc.toObject(Leaderboard::class.java)
                } ?: emptyList()

            trySend(leaderboard)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun updateUserLeaderboardScore(userId: String?, userEmail: String?, score: Long): Boolean{
        if(userId == null){
            return false
        }

        return try {
            val leaderboardCollection = db.collection("leaderboard")

            val querySnapshot = leaderboardCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()

            if(!querySnapshot.isEmpty){
                val document = querySnapshot.documents[0]
                val currentScore = document.getLong("totalScore") ?: 0L

                document.reference.update("totalScore", currentScore + score)
            } else {
                val newDoc = Leaderboard(
                    userId = userId,
                    userEmail = userEmail,
                    totalScore = score
                )
                leaderboardCollection.add(newDoc)
            }
            true
        }
        catch (e: Exception){
            e.printStackTrace()
            false
        }
    }
}

