package com.example.fourquiz.data.local.entity

/** Gemini - início
Prompt: Act as a Senior Android Developer. I have an existing Android project with the namespace com.example.fourquiz. Task: Implement the local persistence layer. Git Start: Provide the command to create and switch to a branch named feature/local-data. Code: Create the Question and UserResult Entities, the DAO (with suspend functions), and the RoomDatabase class with a TypeConverter. Git End: Provide the commands to add all changes and commit with the message 'Add Room persistence layer'. Instructions: Use the namespace com.example.fourquiz. TELL ME EXACTLY the file names and the directory paths (relative to app/src/main/java/com/example/fourquiz/). FORMAT: Wrap all code blocks with: /** Gemini - início \n Prompt: [Insert Prompt Text Here] \n / \n [CODE] \n /* Gemini - final */
*/
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
/* Gemini - final */