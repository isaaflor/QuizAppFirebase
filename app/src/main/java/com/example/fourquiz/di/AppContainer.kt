# --- GEMINI HEADER ---
package com.example.fourquiz.di

import android.content.Context
import androidx.room.Room
import com.example.fourquiz.data.local.QuizDatabase
import com.example.fourquiz.data.remote.FirebaseQuizService
import com.example.fourquiz.data.repository.QuizRepository

/**
 * A simple manual dependency injection container to hold and provide
 * application-level dependencies.
 */
class AppContainer(private val context: Context) {

    // Local Database (Room)
    private val database: QuizDatabase by lazy {
        Room.databaseBuilder(
            context,
            QuizDatabase::class.java,
            "quiz_database"
        ).build()
    }

    // Remote Service (Firebase)
    private val firebaseService: FirebaseQuizService by lazy {
        FirebaseQuizService()
    }

    // Repository (Single Source of Truth)
    val quizRepository: QuizRepository by lazy {
        QuizRepository(
            quizDao = database.quizDao(),
            firebaseService = firebaseService
        )
    }
}
# --- GEMINI FOOTER ---