package com.example.quizapp.model.repository.implementation

import com.example.quizapp.model.Category
import com.example.quizapp.model.repository.CategoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): CategoryRepository {
    override fun getAllCategories(): Flow<List<Category>> = callbackFlow {
        val collection = db.collection("categories")
        val listener = collection.addSnapshotListener {
            snapshot, error ->
            if(error != null){
                close(error)
                return@addSnapshotListener
            }
            if(snapshot != null){
                val items = snapshot.toObjects(Category::class.java)
                trySend(items).isSuccess
            }
        }
        awaitClose {
            listener.remove()
        }
    }
}
