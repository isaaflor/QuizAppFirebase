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
        val listener = db.collection("questions")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val categories = snapshot?.documents
                    ?.mapNotNull { it.getString("categoryId") }
                    ?.distinct()
                    ?.map { categoryId ->
                        Category(id = categoryId, name = categoryId)
                    } ?: emptyList()

                trySend(categories)
            }
        awaitClose { listener.remove() }
    }
}
