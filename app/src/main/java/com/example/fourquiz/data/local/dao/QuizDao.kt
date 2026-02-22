package com.example.fourquiz.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fourquiz.data.local.entity.Question
import com.example.fourquiz.data.local.entity.UserResult

@Dao
interface QuizDao {

// Question Operations
@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertQuestions(questions: List<Question>)

@Query("SELECT * FROM questions")
suspend fun getAllQuestions(): List<Question>

@Query("DELETE FROM questions")
suspend fun clearQuestions()

// UserResult Operations
@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertUserResult(result: UserResult)

@Query("SELECT * FROM user_results ORDER BY timestamp DESC")
suspend fun getAllUserResults(): List<UserResult>
}
