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
