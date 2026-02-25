package com.example.quizapp.di

import com.example.quizapp.model.repository.AuthRepository
import com.example.quizapp.model.repository.CategoryRepository
import com.example.quizapp.model.repository.LeaderboardRepository
import com.example.quizapp.model.repository.QuestionRepository
import com.example.quizapp.model.repository.ResultRepository
import com.example.quizapp.model.repository.implementation.AuthRepositoryImpl
import com.example.quizapp.model.repository.implementation.CategoryRepositoryImpl
import com.example.quizapp.model.repository.implementation.LeaderboardRepositoryImpl
import com.example.quizapp.model.repository.implementation.QuestionRepositoryImpl
import com.example.quizapp.model.repository.implementation.ResultRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object QuizAppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestone(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository = impl

    @Provides
    @Singleton
    fun provideQuestionRepository(impl: QuestionRepositoryImpl): QuestionRepository = impl

    @Provides
    @Singleton
    fun provideResultRepository(impl: ResultRepositoryImpl): ResultRepository = impl

    @Provides
    @Singleton
    fun provideLeaderboardRepository(impl: LeaderboardRepositoryImpl): LeaderboardRepository = impl
}

