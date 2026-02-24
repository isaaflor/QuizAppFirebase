package com.example.quizapp.model.repository

import com.example.quizapp.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository{
    fun getAllCategories(): Flow<List<Category>>
}