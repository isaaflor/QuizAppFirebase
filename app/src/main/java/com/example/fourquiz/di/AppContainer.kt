package com.example.fourquiz.di

import android.content.Context
import com.example.fourquiz.data.local.QuizDatabase
import com.example.fourquiz.data.network.FirebaseAuthService
import com.example.fourquiz.data.network.FirestoreService
import com.example.fourquiz.data.repository.QuizRepository

/**
 * Manual Dependency Injection Container tailored to your Module Manifest.
 */
class AppContainer(private val context: Context) {

    // Local Database (Room) initialized via your getInstance method
    private val database: QuizDatabase by lazy {
        QuizDatabase.getInstance(context)
    }

    // Remote Services (Firebase)
    val firestoreService: FirestoreService by lazy {
        FirestoreService()
    }

    val firebaseAuthService: FirebaseAuthService by lazy {
        FirebaseAuthService()
    }

    // Repository (Single Source of Truth)
    val quizRepository: QuizRepository by lazy {
        QuizRepository(
            quizDao = database.quizDao, // Accessing your abstract property
            firestoreService = firestoreService
        )
    }
}
