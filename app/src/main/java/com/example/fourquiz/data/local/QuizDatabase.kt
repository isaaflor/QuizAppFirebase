package com.example.fourquiz.data.local

/** Gemini - início
Prompt: Act as a Senior Android Developer. I have an existing Android project with the namespace com.example.fourquiz. Task: Implement the local persistence layer. Git Start: Provide the command to create and switch to a branch named feature/local-data. Code: Create the Question and UserResult Entities, the DAO (with suspend functions), and the RoomDatabase class with a TypeConverter. Git End: Provide the commands to add all changes and commit with the message 'Add Room persistence layer'. Instructions: Use the namespace com.example.fourquiz. TELL ME EXACTLY the file names and the directory paths (relative to app/src/main/java/com/example/fourquiz/). FORMAT: Wrap all code blocks with: /** Gemini - início \n Prompt: [Insert Prompt Text Here] \n / \n [CODE] \n /* Gemini - final */
*/
package com.example.fourquiz.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fourquiz.data.local.converter.Converters
import com.example.fourquiz.data.local.dao.QuizDao
import com.example.fourquiz.data.local.entity.Question
import com.example.fourquiz.data.local.entity.UserResult

@Database(
entities = [Question::class, UserResult::class],
version = 1,
exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {

    abstract val quizDao: QuizDao

    companion object {
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        fun getInstance(context: Context): QuizDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                context.applicationContext,
                QuizDatabase::class.java,
                "fourquiz_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
/* Gemini - final */