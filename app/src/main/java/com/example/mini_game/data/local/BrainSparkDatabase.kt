package com.example.mini_game.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [QuizEntity::class, QuestionEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class BrainSparkDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
}
