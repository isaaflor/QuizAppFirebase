package com.example.fourquiz.data.local.dao

/** Gemini - início
Prompt: Act as a Senior Android Developer. I have an existing Android project with the namespace com.example.fourquiz. Task: Implement the local persistence layer. Git Start: Provide the command to create and switch to a branch named feature/local-data. Code: Create the Question and UserResult Entities, the DAO (with suspend functions), and the RoomDatabase class with a TypeConverter. Git End: Provide the commands to add all changes and commit with the message 'Add Room persistence layer'. Instructions: Use the namespace com.example.fourquiz. TELL ME EXACTLY the file names and the directory paths (relative to app/src/main/java/com/example/fourquiz/). FORMAT: Wrap all code blocks with: /** Gemini - início \n Prompt: [Insert Prompt Text Here] \n / \n [CODE] \n /* Gemini - final */
*/
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
/* Gemini - final */