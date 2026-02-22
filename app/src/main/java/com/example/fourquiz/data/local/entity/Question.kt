package com.example.fourquiz.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
@PrimaryKey(autoGenerate = true)
val id: Int = 0,
val text: String,
val options: List<String>, // Requires TypeConverter
val correctAnswerIndex: Int
)
