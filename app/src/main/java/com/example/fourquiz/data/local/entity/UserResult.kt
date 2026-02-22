package com.example.fourquiz.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_results")
data class UserResult(
@PrimaryKey(autoGenerate = true)
val id: Int = 0,
val score: Int,
val totalQuestions: Int,
val timestamp: Long = System.currentTimeMillis()
)
